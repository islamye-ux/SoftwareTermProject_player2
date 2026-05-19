package com.example.swtermproject.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.swtermproject.databinding.ActivityCameraBinding
import com.example.swtermproject.util.Constants

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var viewModel: CameraViewModel
    private var targetLanguage = Constants.DEFAULT_LANGUAGE

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.startCamera(this, binding.previewView)
        else Toast.makeText(this, R.string.camera_permission_required, Toast.LENGTH_SHORT).show()
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.recognizeFromUri(this, it, targetLanguage) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        targetLanguage = intent.getStringExtra(Constants.EXTRA_LANGUAGE_CODE) ?: Constants.DEFAULT_LANGUAGE
        viewModel = ViewModelProvider(this)[CameraViewModel::class.java]

        requestCameraPermissionOrStart()
        setupObservers()
        setupClickListeners()
    }

    private fun requestCameraPermissionOrStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.startCamera(this, binding.previewView)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun setupObservers() {
        viewModel.translationResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            binding.tvOriginal.text = result.originalText
            binding.tvTranslated.text = result.translatedText
            binding.cardResult.visibility = View.VISIBLE
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnCapture.isEnabled = !loading
        }

        viewModel.error.observe(this) { error ->
            error ?: return@observe
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListeners() {
        binding.btnCapture.setOnClickListener {
            binding.cardResult.visibility = View.GONE
            viewModel.captureAndRecognize(this, targetLanguage)
        }

        binding.btnGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnBack.setOnClickListener { finish() }

        binding.btnUseResult.setOnClickListener {
            val result = viewModel.translationResult.value?.translatedText ?: return@setOnClickListener
            setResult(RESULT_OK, Intent().putExtra(Constants.EXTRA_TRANSLATION_RESULT, result))
            finish()
        }
    }
}
