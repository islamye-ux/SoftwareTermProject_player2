package com.example.swtermproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.swtermproject.data.db.dao.ChatHistoryDao
import com.example.swtermproject.data.db.dao.ChatSessionDao
import com.example.swtermproject.data.db.dao.FavoritePlaceDao
import com.example.swtermproject.data.db.dao.SavedPhraseDao
import com.example.swtermproject.data.db.entity.ChatHistoryEntity
import com.example.swtermproject.data.db.entity.ChatSessionEntity
import com.example.swtermproject.data.db.entity.FavoritePlaceEntity
import com.example.swtermproject.data.db.entity.SavedPhraseEntity

@Database(
    entities = [
        FavoritePlaceEntity::class,
        ChatHistoryEntity::class,
        ChatSessionEntity::class,
        SavedPhraseEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritePlaceDao(): FavoritePlaceDao
    abstract fun chatHistoryDao(): ChatHistoryDao
    abstract fun chatSessionDao(): ChatSessionDao
    abstract fun savedPhraseDao(): SavedPhraseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS chat_sessions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    ALTER TABLE chat_history ADD COLUMN sessionId INTEGER NOT NULL DEFAULT 1
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT OR IGNORE INTO chat_sessions (id, title, createdAt, updatedAt)
                    VALUES (1, 'Imported chat', strftime('%s','now')*1000, strftime('%s','now')*1000)
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    UPDATE chat_history SET sessionId = 1 WHERE sessionId IS NULL OR sessionId = 1
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS saved_phrases (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        originalText TEXT NOT NULL,
                        translatedText TEXT NOT NULL,
                        targetLanguage TEXT NOT NULL,
                        savedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kolife_db"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).build().also { INSTANCE = it }
            }
    }
}
