package com.example.ui.screens

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ChatMessageEntity
import com.example.data.model.AIModelTier
import com.example.data.model.UserSubscription
import com.example.ui.components.ChatMessageItem
import com.example.ui.theme.NaturalBackground
import com.example.ui.theme.NaturalBorderMuted
import com.example.ui.theme.NaturalCardBorder
import com.example.ui.theme.NaturalNavBackground
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalPrimaryDark
import com.example.ui.theme.NaturalSagePill
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalSurfaceVariant
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary
import com.example.ui.theme.NaturalTextTertiary

@Composable
fun ChatScreen(
    messages: List<ChatMessageEntity>,
    inputText: String,
    isGenerating: Boolean,
    activeModelTier: AIModelTier,
    subscription: UserSubscription,
    onInputChanged: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onModelSelected: (AIModelTier) -> Unit,
    onClearChat: () -> Unit,
    onDeleteMessage: (Long) -> Unit,
    onSpeakMessage: (String) -> Unit,
    onOpenVoiceMode: () -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val suggestionChips = listOf(
        "🌿 Design a clean natural UI",
        "🎬 Generate 4K AI Video Prompt",
        "💡 Explain Quantum AI simply",
        "🌐 Translate to Spanish & French",
        "🐞 Debug my SQLite database",
        "📄 Summarize document specs"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NaturalBackground)
            .imePadding()
    ) {
        // Model Selection & Clear Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Model selector chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ModelPill(
                    title = "Flash 3.5",
                    isSelected = activeModelTier == AIModelTier.GEMINI_FLASH,
                    isPro = false,
                    onClick = { onModelSelected(AIModelTier.GEMINI_FLASH) },
                    testTag = "model_flash_chip"
                )

                ModelPill(
                    title = "Pro 3.1",
                    isSelected = activeModelTier == AIModelTier.GEMINI_PRO,
                    isPro = true,
                    onClick = { onModelSelected(AIModelTier.GEMINI_PRO) },
                    testTag = "model_pro_chip"
                )
            }

            IconButton(
                onClick = onClearChat,
                modifier = Modifier
                    .size(34.dp)
                    .testTag("clear_chat_button")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteSweep,
                    contentDescription = "Clear Chat",
                    tint = NaturalTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(items = messages, key = { it.id }) { message ->
                ChatMessageItem(
                    message = message,
                    onSpeak = onSpeakMessage,
                    onDelete = onDeleteMessage
                )
            }

            if (isGenerating) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = NaturalPrimary,
                            strokeWidth = 2.5.dp
                        )
                        Text(
                            text = "Nova AI is generating response...",
                            fontSize = 13.sp,
                            color = NaturalPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Suggestion Chips (shown when few messages)
        if (messages.size <= 2) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(suggestionChips) { chipText ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = NaturalSurface,
                        modifier = Modifier
                            .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                            .clickable { onSendMessage(chipText) }
                    ) {
                        Text(
                            text = chipText,
                            color = NaturalTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Chat Input Bar
        Surface(
            color = NaturalNavBackground,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = NaturalBorderMuted,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                ),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Voice Mic Button
                IconButton(
                    onClick = onOpenVoiceMode,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(NaturalSurfaceVariant)
                        .testTag("voice_mode_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Mode",
                        tint = NaturalPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Text Input
                OutlinedTextField(
                    value = inputText,
                    onValueChange = onInputChanged,
                    placeholder = {
                        Text(
                            text = "Ask Nova AI anything...",
                            color = NaturalTextTertiary,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = NaturalTextPrimary,
                        unfocusedTextColor = NaturalTextPrimary,
                        focusedBorderColor = NaturalPrimary,
                        unfocusedBorderColor = NaturalCardBorder,
                        focusedContainerColor = NaturalSurface,
                        unfocusedContainerColor = NaturalSurface
                    ),
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { onSendMessage(inputText) })
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Send Button
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            if (inputText.isNotBlank() && !isGenerating) NaturalPrimary
                            else NaturalSurfaceVariant
                        )
                        .clickable(enabled = inputText.isNotBlank() && !isGenerating) {
                            onSendMessage(inputText)
                        }
                        .testTag("send_message_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank() && !isGenerating) Color.White else NaturalTextTertiary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ModelPill(
    title: String,
    isSelected: Boolean,
    isPro: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) NaturalSagePill else NaturalSurface,
        modifier = Modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = if (isSelected) NaturalPrimary else NaturalCardBorder,
                shape = RoundedCornerShape(14.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (isPro) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = "Pro",
                    tint = NaturalPrimary,
                    modifier = Modifier.size(13.dp)
                )
            }
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) NaturalPrimary else NaturalTextSecondary
            )
        }
    }
}
