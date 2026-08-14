package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val role: String, // "user" or "assistant"
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val model: String = "gemini-3.5-flash",
    val category: String = "chat", // "chat", "code", "image", "video", "translate", "doc"
    val mediaUrl: String? = null,
    val codeLanguage: String? = null
)

@Entity(tableName = "saved_creations")
data class SavedCreationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "IMAGE", "VIDEO", "CODE", "DOCUMENT"
    val title: String,
    val prompt: String,
    val contentOrUrl: String,
    val resolution: String? = null,
    val style: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
