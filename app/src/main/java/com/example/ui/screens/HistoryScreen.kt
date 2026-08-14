package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SavedCreationEntity
import com.example.ui.theme.NaturalBackground
import com.example.ui.theme.NaturalCardBorder
import com.example.ui.theme.NaturalLimeAccent
import com.example.ui.theme.NaturalLimeDarkText
import com.example.ui.theme.NaturalNavBackground
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalPrimaryDark
import com.example.ui.theme.NaturalSagePill
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalSurfaceVariant
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary
import com.example.ui.theme.NaturalTextTertiary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    creations: List<SavedCreationEntity>,
    onDeleteCreation: (Long) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredCreations = when (selectedFilter) {
        "IMAGE" -> creations.filter { it.type == "IMAGE" }
        "VIDEO" -> creations.filter { it.type == "VIDEO" }
        "FAVORITES" -> creations.filter { it.isFavorite }
        else -> creations
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NaturalBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = NaturalPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Creations History",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier.testTag("close_history_btn")
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = NaturalTextPrimary)
            }
        }

        // Filter Pills
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterPill("All (${creations.size})", selectedFilter == "ALL") { selectedFilter = "ALL" }
            FilterPill("Videos", selectedFilter == "VIDEO") { selectedFilter = "VIDEO" }
            FilterPill("Images", selectedFilter == "IMAGE") { selectedFilter = "IMAGE" }
            FilterPill("Favorites", selectedFilter == "FAVORITES") { selectedFilter = "FAVORITES" }
        }

        if (filteredCreations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = NaturalTextTertiary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No creations found in history yet",
                        color = NaturalTextSecondary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Generate videos or images to save them here automatically",
                        color = NaturalTextTertiary,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items = filteredCreations, key = { it.id }) { item ->
                    CreationHistoryCard(
                        creation = item,
                        onDelete = { onDeleteCreation(item.id) },
                        onFavorite = { onToggleFavorite(item.id) },
                        onCopyPrompt = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Prompt", item.prompt)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Prompt copied", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterPill(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) NaturalSagePill else NaturalSurface,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .border(
                1.dp,
                if (isSelected) NaturalPrimary else NaturalCardBorder.copy(alpha = 0.6f),
                RoundedCornerShape(12.dp)
            )
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) NaturalPrimary else NaturalTextSecondary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun CreationHistoryCard(
    creation: SavedCreationEntity,
    onDelete: () -> Unit,
    onFavorite: () -> Unit,
    onCopyPrompt: () -> Unit
) {
    val dateStr = SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault()).format(Date(creation.timestamp))
    val isVideo = creation.type == "VIDEO"

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NaturalSurface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(NaturalSagePill),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isVideo) Icons.Default.Videocam else Icons.Default.Image,
                            contentDescription = null,
                            tint = NaturalPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Column {
                        Text(
                            text = creation.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NaturalTextPrimary
                        )
                        Text(
                            text = "$dateStr • ${creation.resolution ?: "HD"}",
                            fontSize = 10.sp,
                            color = NaturalTextSecondary
                        )
                    }
                }

                Row {
                    IconButton(onClick = onCopyPrompt, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = NaturalTextSecondary, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onFavorite, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = if (creation.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (creation.isFavorite) NaturalPrimary else NaturalTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = NaturalTextSecondary, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = creation.prompt,
                fontSize = 12.sp,
                color = NaturalTextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}
