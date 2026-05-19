package com.example.swtermproject.ui.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swtermproject.data.db.entity.FavoritePlaceEntity
import com.example.swtermproject.data.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    private lateinit var repository: FavoriteRepository
    lateinit var favorites: LiveData<List<FavoritePlaceEntity>>
        private set

    fun init(context: Context) {
        repository = FavoriteRepository(context)
        favorites = repository.getFavorites()
    }

    fun removeFavorite(place: FavoritePlaceEntity) {
        viewModelScope.launch { repository.removeFavorite(place) }
    }
}
