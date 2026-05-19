package com.example.swtermproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.swtermproject.data.db.dao.ChatHistoryDao
import com.example.swtermproject.data.db.dao.FavoritePlaceDao
import com.example.swtermproject.data.db.entity.ChatHistoryEntity
import com.example.swtermproject.data.db.entity.FavoritePlaceEntity

@Database(
    entities = [FavoritePlaceEntity::class, ChatHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritePlaceDao(): FavoritePlaceDao
    abstract fun chatHistoryDao(): ChatHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kolife_db"
                ).build().also { INSTANCE = it }
            }
    }
}
