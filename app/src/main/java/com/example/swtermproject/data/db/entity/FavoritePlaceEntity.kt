package com.example.swtermproject.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_places")
data class FavoritePlaceEntity(
    @PrimaryKey val placeId: String,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val rating: Float,
    val category: String,
    val savedAt: Long = System.currentTimeMillis()
)
