package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ChatMessageEntity
import com.example.ui.theme.NaturalCardBorder
import com.example.ui.theme.NaturalNavBackground
import com.example.ui.theme.NaturalPrimary
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
fun ChatMessageItem(
    message: ChatMessageEntity,
    onSpeak: (String) -> Unit,
    onDelete: (Long) -> Unit
) {
    val context = LocalContext.current
    val isUser = message.role == "user"
    val timeStr = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(message.timestamp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            // Assistant Botanical Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(NaturalPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Nova AI",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.fillMaxWidth(if (isUser) 0.82f else 0.88f),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            // Natural Tones Bubble: Assistant -> NaturalSurfaceVariant (#E2E3DE), User -> NaturalSagePill (#DCE5CC)
            Surface(
                shape = RoundedCornerShape(
                    topStart = if (!isUser) 4.dp else 18.dp,
                    topEnd = if (isUser) 4.dp else 18.dp,
                    bottomStart = 18.dp,
                    bottomEnd = 18.dp
                ),
                color = if (isUser) NaturalSagePill else NaturalSurfaceVariant,
                modifier = Modifier.border(
                    width = 0.5.dp,
                    color = NaturalCardBorder.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(
                        topStart = if (!isUser) 4.dp else 18.dp,
                        topEnd = if (isUser) 4.dp else 18.dp,
                        bottomStart = 18.dp,
                        bottomEnd = 18.dp
                    )
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    if (!isUser) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Nova AI",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalPrimary
                            )
                            Text(
                                text = timeStr,
                                fontSize = 10.sp,
                                color = NaturalTextTertiary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // Render Message Content
                    RenderMessageBody(content = message.content, isUser = isUser)
                }
            }

            // Action row below message (Copy, Speak, Delete)
            if (!isUser) {
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Nova AI Response", message.content)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy text",
                            tint = NaturalTextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    IconButton(
                        onClick = { onSpeak(message.content) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Speak aloud",
                            tint = NaturalTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = { onDelete(message.id) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete message",
                            tint = NaturalTextTertiary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            } else {
                Text(
                    text = timeStr,
                    fontSize = 10.sp,
                    color = NaturalTextTertiary,
                    modifier = Modifier.padding(top = 2.dp, end = 4.dp)
                )
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            // User Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(NaturalPrimary.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun RenderMessageBody(content: String, isUser: Boolean) {
    if (content.contains("```")) {
        val parts = content.split("```")
        Column {
            parts.forEachIndexed { index, part ->
                if (index % 2 == 1) {
                    val lines = part.trim().lines()
                    val lang = if (lines.isNotEmpty() && !lines.first().contains(" ")) lines.first() else "Code"
                    val codeContent = if (lines.isNotEmpty() && !lines.first().contains(" ")) {
                        lines.drop(1).joinToString("\n")
                    } else {
                        part
                    }

                    SyntaxCodeBox(code = codeContent, language = lang)
                } else if (part.isNotBlank()) {
                    Text(
                        text = part.trim(),
                        color = NaturalTextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }
        }
    } else {
        Text(
            text = content,
            color = NaturalTextPrimary,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
    }
}

@Composable
fun SyntaxCodeBox(code: String, language: String) {
    val context = LocalContext.current
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = NaturalSurface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(1.dp, NaturalCardBorder, RoundedCornerShape(10.dp))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NaturalNavBackground)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = language.uppercase(),
                    color = NaturalPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Code", code)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Code copied!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy code",
                        tint = NaturalTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Text(
                text = code,
                color = NaturalTextPrimary,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 18.sp,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}
