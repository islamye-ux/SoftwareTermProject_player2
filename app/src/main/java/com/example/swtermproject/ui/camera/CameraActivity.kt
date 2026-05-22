package com.example.swtermproject.ui.camera

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import com.example.swtermproject.R
import com.example.swtermproject.databinding.ActivityCameraBinding
import com.example.swtermproject.data.repository.SavedPhraseRepository
import com.example.swtermproject.util.Constants
import kotlinx.coroutines.launch

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var viewModel: CameraViewModel
    private lateinit var savedPhraseRepository: SavedPhraseRepository
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
        savedPhraseRepository = SavedPhraseRepository(applicationContext)

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

        binding.btnCopyOriginal.setOnClickListener {
            val text = viewModel.translationResult.value?.originalText ?: return@setOnClickListener
            copyToClipboard(getString(R.string.label_original), text)
        }

        binding.btnCopyTranslated.setOnClickListener {
            val text = viewModel.translationResult.value?.translatedText ?: return@setOnClickListener
            copyToClipboard(getString(R.string.label_translated), text)
        }

        binding.btnSavePhrase.setOnClickListener {
            val result = viewModel.translationResult.value ?: return@setOnClickListener
            lifecycleScope.launch {
                savedPhraseRepository.addPhrase(
                    original = result.originalText,
                    translated = result.translatedText,
                    targetLanguage = targetLanguage
                )
                Toast.makeText(this@CameraActivity, R.string.saved_to_saved, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun copyToClipboard(label: String, text: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
        Toast.makeText(this, R.string.copied, Toast.LENGTH_SHORT).show()
    }
}
