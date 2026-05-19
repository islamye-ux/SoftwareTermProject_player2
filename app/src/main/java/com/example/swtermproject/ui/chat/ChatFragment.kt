package com.example.swtermproject.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swtermproject.databinding.FragmentChatBinding
import com.google.android.material.chip.Chip

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: ChatAdapter

    private val quickTemplates = listOf(
        "How do I extend my visa?",
        "How to sign up for health insurance?",
        "How to open a bank account?",
        "Nearest subway station directions",
        "Emergency numbers in Korea",
        "How to register as a foreigner?"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        viewModel.init(requireContext())

        setupRecyclerView()
        setupObservers()
        setupInput()
        setupQuickTemplates()
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter()
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(context).apply { stackFromEnd = true }
            adapter = this@ChatFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            adapter.submitList(messages.toList())
            if (messages.isNotEmpty()) binding.rvMessages.smoothScrollToPosition(messages.size - 1)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.btnSend.isEnabled = !loading
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun setupInput() {
        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text?.toString()?.trim() ?: return@setOnClickListener
            if (text.isEmpty()) return@setOnClickListener
            viewModel.sendMessage(text)
            binding.etMessage.text?.clear()
        }
    }

    private fun setupQuickTemplates() {
        quickTemplates.forEach { template ->
            val chip = Chip(requireContext()).apply {
                text = template
                isClickable = true
                setOnClickListener {
                    viewModel.sendMessage(template)
                }
            }
            binding.chipGroupTemplates.addView(chip)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
