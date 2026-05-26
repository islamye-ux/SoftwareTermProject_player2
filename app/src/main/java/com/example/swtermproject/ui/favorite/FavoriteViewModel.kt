package com.example.swtermproject.ui.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swtermproject.data.db.entity.FavoritePlaceEntity
import com.example.swtermproject.data.db.entity.SavedPhraseEntity
import com.example.swtermproject.data.repository.FavoriteRepository
import com.example.swtermproject.data.repository.SavedPhraseRepository
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    private lateinit var repository: FavoriteRepository
    private lateinit var phraseRepository: SavedPhraseRepository
    lateinit var favorites: LiveData<List<FavoritePlaceEntity>>
        private set
    lateinit var savedPhrases: LiveData<List<SavedPhraseEntity>>
        private set

    fun init(context: Context) {
        repository = FavoriteRepository(context)
        phraseRepository = SavedPhraseRepository(context)
        favorites = repository.getFavorites()
        savedPhrases = phraseRepository.getAll()
    }

    fun removeFavorite(place: FavoritePlaceEntity) {
        viewModelScope.launch { repository.removeFavorite(place) }
    }

    fun removeSavedPhrase(phrase: SavedPhraseEntity) {
        viewModelScope.launch { phraseRepository.removeById(phrase.id) }
    }
}
