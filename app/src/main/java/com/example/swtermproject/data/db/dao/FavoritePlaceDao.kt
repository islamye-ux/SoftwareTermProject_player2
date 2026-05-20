package com.example.swtermproject.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.swtermproject.data.db.entity.FavoritePlaceEntity

@Dao
interface FavoritePlaceDao {
    @Query("SELECT * FROM favorite_places ORDER BY savedAt DESC")
    fun getAll(): LiveData<List<FavoritePlaceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: FavoritePlaceEntity)

    @Delete
    suspend fun delete(place: FavoritePlaceEntity)

    @Query("DELETE FROM favorite_places WHERE placeId = :placeId")
    suspend fun deleteById(placeId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_places WHERE placeId = :placeId)")
    suspend fun isFavorite(placeId: String): Boolean
}
