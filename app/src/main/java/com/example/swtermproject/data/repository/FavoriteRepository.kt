package com.example.swtermproject.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.swtermproject.data.db.AppDatabase
import com.example.swtermproject.data.db.entity.FavoritePlaceEntity
import com.example.swtermproject.data.model.PlaceModel

class FavoriteRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).favoritePlaceDao()

    fun getFavorites(): LiveData<List<FavoritePlaceEntity>> = dao.getAll()

    suspend fun addFavorite(place: PlaceModel) {
        dao.insert(
            FavoritePlaceEntity(
                placeId = place.id,
                name = place.name,
                address = place.address,
                lat = place.lat,
                lng = place.lng,
                rating = place.rating,
                category = place.category
            )
        )
    }

    suspend fun removeFavorite(place: FavoritePlaceEntity) = dao.delete(place)

    suspend fun isFavorite(placeId: String) = dao.isFavorite(placeId)
}
