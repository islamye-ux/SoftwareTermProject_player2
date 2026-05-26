package com.example.swtermproject.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swtermproject.databinding.FragmentChatBinding
import com.example.swtermproject.data.db.entity.ChatSessionEntity
import com.google.android.material.chip.Chip
import androidx.core.view.GravityCompat

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: ChatAdapter
    private lateinit var sessionAdapter: ChatSessionAdapter

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
        setupSessionDrawer()
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

    private fun setupSessionDrawer() {
        sessionAdapter = ChatSessionAdapter(
            onClick = { session ->
                viewModel.selectSession(session.id)
                binding.chatDrawerLayout.closeDrawer(GravityCompat.END)
            },
            onMoreClick = { session -> showSessionOptions(session) }
        )
        binding.rvChatSessions.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = sessionAdapter
        }

        binding.btnOpenChats.setOnClickListener {
            binding.chatDrawerLayout.openDrawer(GravityCompat.END)
        }
        binding.btnNewChat.setOnClickListener {
            viewModel.newChat()
            binding.etChatSearch.text?.clear()
            binding.chatDrawerLayout.closeDrawer(GravityCompat.END)
        }
        binding.btnClearChat.setOnClickListener {
            showClearChatDialog()
        }
        binding.etChatSearch.addTextChangedListener { text ->
            viewModel.searchSessions(text?.toString())
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

        viewModel.sessions.observe(viewLifecycleOwner) { sessions ->
            sessionAdapter.submitList(sessions)
        }

        viewModel.currentSessionId.observe(viewLifecycleOwner) { sessionId ->
            sessionAdapter.setSelected(sessionId)
        }

        viewModel.currentSessionTitle.observe(viewLifecycleOwner) { title ->
            binding.tvChatTitle.text = title
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

    private fun showSessionOptions(session: ChatSessionEntity) {
        val options = arrayOf(
            getString(com.example.swtermproject.R.string.rename_chat),
            getString(com.example.swtermproject.R.string.delete_chat)
        )
        AlertDialog.Builder(requireContext())
            .setTitle(session.title)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showRenameDialog(session)
                    1 -> showDeleteDialog(session)
                }
            }
            .setNegativeButton(com.example.swtermproject.R.string.cancel, null)
            .show()
    }

    private fun showRenameDialog(session: ChatSessionEntity) {
        val input = EditText(requireContext()).apply {
            setText(session.title)
            setSelection(text.length)
        }
        AlertDialog.Builder(requireContext())
            .setTitle(com.example.swtermproject.R.string.rename_chat)
            .setView(input)
            .setPositiveButton(com.example.swtermproject.R.string.save) { _, _ ->
                val newTitle = input.text?.toString()?.trim().orEmpty()
                if (newTitle.isNotEmpty()) {
                    viewModel.renameSession(session.id, newTitle)
                }
            }
            .setNegativeButton(com.example.swtermproject.R.string.cancel, null)
            .show()
    }

    private fun showDeleteDialog(session: ChatSessionEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle(com.example.swtermproject.R.string.delete_chat)
            .setMessage(com.example.swtermproject.R.string.delete_chat_confirm)
            .setPositiveButton(com.example.swtermproject.R.string.delete) { _, _ ->
                viewModel.deleteSession(session.id)
            }
            .setNegativeButton(com.example.swtermproject.R.string.cancel, null)
            .show()
    }

    private fun showClearChatDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(com.example.swtermproject.R.string.clear_chat)
            .setMessage(com.example.swtermproject.R.string.clear_chat_confirm)
            .setPositiveButton(com.example.swtermproject.R.string.clear) { _, _ ->
                viewModel.clearChatMessages()
                binding.chatDrawerLayout.closeDrawer(GravityCompat.END)
            }
            .setNegativeButton(com.example.swtermproject.R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
