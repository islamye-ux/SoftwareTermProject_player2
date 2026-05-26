package com.example.swtermproject.ui.phrase

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swtermproject.R
import com.example.swtermproject.data.repository.SavedPhraseRepository
import com.example.swtermproject.databinding.FragmentPhraseBinding
import com.example.swtermproject.ui.favorite.SavedPhraseAdapter

class PhraseFragment : Fragment() {

    private var _binding: FragmentPhraseBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TranslatorViewModel
    private lateinit var recentsAdapter: SavedPhraseAdapter

    private val languages = listOf(
        "Auto-detect" to "",
        "English" to "en",
        "Korean" to "ko",
        "Japanese" to "ja",
        "Chinese (Simplified)" to "zh-CN",
        "Vietnamese" to "vi"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPhraseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TranslatorViewModel::class.java]

        // Spinners
        val names = languages.map { it.first }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, names)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSource.adapter = adapter
        binding.spinnerTarget.adapter = adapter
        binding.spinnerSource.setSelection(0)
        binding.spinnerTarget.setSelection(1)

        binding.btnSwap.setOnClickListener {
            val s = binding.spinnerSource.selectedItemPosition
            val t = binding.spinnerTarget.selectedItemPosition
            binding.spinnerSource.setSelection(t)
            binding.spinnerTarget.setSelection(s)
        }

        binding.btnTranslate.setOnClickListener {
            val text = binding.etInput.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(requireContext(), R.string.no_text_detected, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val targetIdx = binding.spinnerTarget.selectedItemPosition
            val targetCode = languages[targetIdx].second.ifEmpty { "en" }
            viewModel.translate(text, targetCode)
        }

        // Recents
        recentsAdapter = SavedPhraseAdapter { phrase -> viewModel.removeSavedPhrase(phrase) }
        binding.rvPhrases.layoutManager = LinearLayoutManager(context)
        binding.rvPhrases.adapter = recentsAdapter

        viewModel.savedPhrases.observe(viewLifecycleOwner) { phrases ->
            recentsAdapter.submitList(phrases)
        }

        viewModel.translated.observe(viewLifecycleOwner) { translated ->
            binding.tvOutput.text = translated
            // copy to clipboard automatically
            val clipboard = requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("Translation", translated))
            Toast.makeText(requireContext(), R.string.copied, Toast.LENGTH_SHORT).show()
        }

        viewModel.error.observe(viewLifecycleOwner) { err ->
            err?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
