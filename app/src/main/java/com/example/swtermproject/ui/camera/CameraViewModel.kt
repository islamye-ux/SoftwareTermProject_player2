package com.example.swtermproject.ui.camera

import android.content.Context
import android.net.Uri
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swtermproject.data.model.TranslationResult
import com.example.swtermproject.data.repository.TranslateRepository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel() {

    private val _translationResult = MutableLiveData<TranslationResult>()
    val translationResult: LiveData<TranslationResult> = _translationResult

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val translateRepository = TranslateRepository()
    private val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
    private var imageCapture: ImageCapture? = null

    fun startCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        val future = ProcessCameraProvider.getInstance(previewView.context)
        future.addListener({
            val cameraProvider = future.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                _error.value = "Camera init failed: ${e.message}"
            }
        }, ContextCompat.getMainExecutor(previewView.context))
    }

    fun captureAndRecognize(context: Context, targetLanguage: String) {
        val capture = imageCapture ?: return
        _isLoading.value = true
        capture.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val inputImage = InputImage.fromMediaImage(
                        image.image!!, image.imageInfo.rotationDegrees
                    )
                    processImage(inputImage, targetLanguage)
                    image.close()
                }

                override fun onError(e: ImageCaptureException) {
                    _isLoading.value = false
                    _error.value = "Capture failed: ${e.message}"
                }
            }
        )
    }

    fun recognizeFromUri(context: Context, uri: Uri, targetLanguage: String) {
        _isLoading.value = true
        try {
            val inputImage = InputImage.fromFilePath(context, uri)
            processImage(inputImage, targetLanguage)
        } catch (e: Exception) {
            _isLoading.value = false
            _error.value = "Failed to load image: ${e.message}"
        }
    }

    private fun processImage(image: InputImage, targetLanguage: String) {
        recognizer.process(image)
            .addOnSuccessListener { result ->
                val text = result.text
                if (text.isBlank()) {
                    _isLoading.value = false
                    _error.value = "No Korean text detected. Try again."
                    return@addOnSuccessListener
                }
                viewModelScope.launch {
                    try {
                        val translation = translateRepository.translate(text, targetLanguage)
                        _translationResult.value = translation
                    } catch (e: Exception) {
                        _error.value = "Translation failed: ${e.message}"
                    } finally {
                        _isLoading.value = false
                    }
                }
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                _error.value = "Recognition failed: ${e.message}"
            }
    }
}
