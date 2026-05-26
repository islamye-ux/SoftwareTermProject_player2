package com.example.swtermproject.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swtermproject.data.db.entity.FavoritePlaceEntity
import com.example.swtermproject.data.db.entity.SavedPhraseEntity
import com.example.swtermproject.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var placeAdapter: FavoriteAdapter
    private lateinit var phraseAdapter: SavedPhraseAdapter
    private var cachedPlaces: List<FavoritePlaceEntity> = emptyList()
    private var cachedPhrases: List<SavedPhraseEntity> = emptyList()
    private var showingPhrases = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        viewModel.init(requireContext())

        placeAdapter = FavoriteAdapter { place -> viewModel.removeFavorite(place) }
        phraseAdapter = SavedPhraseAdapter { phrase -> viewModel.removeSavedPhrase(phrase) }
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FavoriteFragment.phraseAdapter
        }

        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            cachedPlaces = favorites
            if (!showingPhrases) {
                placeAdapter.submitList(favorites)
                updateEmptyState()
            }
        }

        viewModel.savedPhrases.observe(viewLifecycleOwner) { phrases ->
            cachedPhrases = phrases
            if (showingPhrases) {
                phraseAdapter.submitList(phrases)
                updateEmptyState()
            }
        }

        binding.chipSavedPhrases.setOnClickListener {
            showingPhrases = true
            binding.rvFavorites.adapter = phraseAdapter
            phraseAdapter.submitList(cachedPhrases)
            updateEmptyState()
        }
        binding.chipSavedPlaces.setOnClickListener {
            showingPhrases = false
            binding.rvFavorites.adapter = placeAdapter
            placeAdapter.submitList(cachedPlaces)
            updateEmptyState()
        }

        binding.chipSavedPhrases.isChecked = true
        phraseAdapter.submitList(cachedPhrases)
        updateEmptyState()
    }

    private fun updateEmptyState() {
        val empty = if (showingPhrases) cachedPhrases.isEmpty() else cachedPlaces.isEmpty()
        val emptyText = if (showingPhrases) {
            getString(com.example.swtermproject.R.string.empty_saved_translations)
        } else {
            getString(com.example.swtermproject.R.string.empty_favorites)
        }
        binding.tvEmpty.text = emptyText
        binding.tvEmpty.visibility = if (empty) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
