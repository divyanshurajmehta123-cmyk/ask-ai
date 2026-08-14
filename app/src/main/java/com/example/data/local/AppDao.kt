package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Chat Messages
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages WHERE category = :category ORDER BY timestamp ASC")
    fun getMessagesByCategory(category: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity): Long

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatHistory()

    @Query("DELETE FROM chat_messages WHERE id = :id")
    suspend fun deleteMessageById(id: Long)

    // Saved Creations (Images, Videos, Code, Docs)
    @Query("SELECT * FROM saved_creations ORDER BY timestamp DESC")
    fun getAllCreations(): Flow<List<SavedCreationEntity>>

    @Query("SELECT * FROM saved_creations WHERE type = :type ORDER BY timestamp DESC")
    fun getCreationsByType(type: String): Flow<List<SavedCreationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreation(creation: SavedCreationEntity): Long

    @Query("DELETE FROM saved_creations WHERE id = :id")
    suspend fun deleteCreationById(id: Long)

    @Query("UPDATE saved_creations SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Long)
}
