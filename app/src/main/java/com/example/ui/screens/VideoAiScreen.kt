package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SavedCreationEntity
import com.example.data.model.UserSubscription
import com.example.data.model.VideoPreset
import com.example.data.model.VideoResolution
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

@Composable
fun VideoAiScreen(
    prompt: String,
    selectedResolution: VideoResolution,
    selectedCameraStyle: String,
    selectedDuration: String,
    isRendering: Boolean,
    renderProgress: Float,
    renderStage: String,
    latestVideo: SavedCreationEntity?,
    subscription: UserSubscription,
    presets: List<VideoPreset>,
    onPromptChanged: (String) -> Unit,
    onResolutionSelected: (VideoResolution) -> Unit,
    onCameraStyleSelected: (String) -> Unit,
    onDurationSelected: (String) -> Unit,
    onGenerateClicked: (String) -> Unit,
    onOpenUpgradeModal: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }

    val cameraStyles = listOf(
        "Cinematic Drone 3D",
        "360 Orbit Camera",
        "Hyperlapse 60fps",
        "Slow Motion Dolly",
        "Macro Close-up"
    )

    val durations = listOf("5s", "10s", "15s")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NaturalBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = null,
                    tint = NaturalPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Veo 4K Video AI Engine",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
            }

            Surface(
                color = if (subscription.allows4KVideo) NaturalLimeAccent else NaturalSagePill,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (subscription.allows4KVideo) Icons.Default.WorkspacePremium else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (subscription.allows4KVideo) NaturalLimeDarkText else NaturalPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = if (subscription.allows4KVideo) "4K UNLOCKED" else "4K LOCKED (PRO)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (subscription.allows4KVideo) NaturalLimeDarkText else NaturalPrimary
                    )
                }
            }
        }

        // Prompt Input
        OutlinedTextField(
            value = prompt,
            onValueChange = onPromptChanged,
            placeholder = {
                Text(
                    text = "Describe your cinematic scene, movement, dynamic lighting...",
                    color = NaturalTextTertiary,
                    fontSize = 14.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(105.dp)
                .testTag("video_prompt_field"),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = NaturalTextPrimary,
                unfocusedTextColor = NaturalTextPrimary,
                focusedBorderColor = NaturalPrimary,
                unfocusedBorderColor = NaturalCardBorder,
                focusedContainerColor = NaturalSurface,
                unfocusedContainerColor = NaturalSurface
            )
        )

        // Resolution Selector
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Render Resolution",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NaturalTextSecondary
                )
                Text(
                    text = "4K requires Premium (₹1,999)",
                    fontSize = 11.sp,
                    color = NaturalPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                VideoResolution.values().forEach { res ->
                    val isSelected = selectedResolution == res
                    val isLocked = res.isPro && !subscription.allows4KVideo

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) {
                            if (res.isPro) NaturalLimeAccent else NaturalSagePill
                        } else NaturalSurface,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                if (isLocked) {
                                    onOpenUpgradeModal()
                                } else {
                                    onResolutionSelected(res)
                                }
                            }
                            .border(
                                width = 1.dp,
                                color = if (isSelected) NaturalPrimary else NaturalCardBorder.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .testTag("resolution_chip_${res.name}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (res.isPro) {
                                    Icon(
                                        imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.WorkspacePremium,
                                        contentDescription = null,
                                        tint = if (isSelected) NaturalLimeDarkText else NaturalPrimary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                }
                                Text(
                                    text = res.label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) (if (res.isPro) NaturalLimeDarkText else NaturalPrimary) else (if (isLocked) NaturalTextTertiary else NaturalTextPrimary)
                                )
                            }
                            Text(
                                text = if (res.isPro) "Ultra HD" else "Standard",
                                fontSize = 10.sp,
                                color = if (isSelected && res.isPro) NaturalLimeDarkText else NaturalTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Camera Motion Style Selector
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Camera & Motion Style",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = NaturalTextSecondary
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(cameraStyles) { style ->
                    val isSelected = selectedCameraStyle == style
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) NaturalSagePill else NaturalSurface,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onCameraStyleSelected(style) }
                            .border(
                                width = 1.dp,
                                color = if (isSelected) NaturalPrimary else NaturalCardBorder.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = if (isSelected) NaturalPrimary else NaturalTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = style,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) NaturalPrimary else NaturalTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Duration Selector
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Duration",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = NaturalTextSecondary
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                durations.forEach { dur ->
                    val isSelected = selectedDuration == dur
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) NaturalSagePill else NaturalSurface,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onDurationSelected(dur) }
                            .border(
                                width = 1.dp,
                                color = if (isSelected) NaturalPrimary else NaturalCardBorder.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(
                            text = dur,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) NaturalPrimary else NaturalTextSecondary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Generate Video Button
        Button(
            onClick = { onGenerateClicked(prompt) },
            enabled = !isRendering && (prompt.isNotBlank() || latestVideo == null),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_video_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NaturalPrimary,
                contentColor = Color.White
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = if (isRendering) "Rendering AI Video..." else "Generate Veo Video (${selectedResolution.label})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        // Rendering Progress Bar Visualizer
        if (isRendering) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NaturalPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = renderStage,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalPrimary
                        )
                        Text(
                            text = "${(renderProgress * 100).toInt()}%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextPrimary
                        )
                    }

                    LinearProgressIndicator(
                        progress = { renderProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = NaturalPrimary,
                        trackColor = NaturalSagePill
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = NaturalPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (subscription.isPremium) "Priority GPU Cluster Allocated (Pro VIP)" else "Standard Queue",
                            fontSize = 11.sp,
                            color = NaturalTextSecondary
                        )
                    }
                }
            }
        }

        // Interactive Video Player Simulator Canvas
        if (latestVideo != null || isRendering) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Veo Output Player",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTextPrimary
                            )
                            Surface(
                                color = NaturalSagePill,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = latestVideo?.resolution ?: selectedResolution.label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = {
                                    Toast.makeText(context, "Exporting 4K MP4 to Videos folder...", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Export MP4",
                                    tint = NaturalTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Video Playback Canvas
                    VideoPlaybackCanvas(
                        isPlaying = isPlaying && !isRendering,
                        resolution = latestVideo?.resolution ?: selectedResolution.label,
                        onTogglePlay = { isPlaying = !isPlaying }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = latestVideo?.prompt ?: prompt,
                        fontSize = 11.sp,
                        color = NaturalTextSecondary,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Video Presets
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Cinematic Video Prompts",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalTextPrimary
            )

            presets.forEach { preset ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NaturalSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                        .clickable {
                            onPromptChanged(preset.promptTemplate)
                            onCameraStyleSelected(preset.motion)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NaturalSagePill),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                tint = NaturalPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = preset.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = NaturalTextPrimary
                            )
                            Text(
                                text = preset.promptTemplate,
                                fontSize = 11.sp,
                                color = NaturalTextSecondary,
                                maxLines = 1
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Use preset",
                            tint = NaturalPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun VideoPlaybackCanvas(
    isPlaying: Boolean,
    resolution: String,
    onTogglePlay: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "videoPlay")
    val animOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "videoOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NaturalPrimaryDark)
            .clickable { onTogglePlay() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Background dynamic atmospheric gradient
            val bgGradient = Brush.linearGradient(
                colors = listOf(
                    NaturalPrimaryDark,
                    NaturalPrimary,
                    Color(0xFF1E3A10)
                )
            )
            drawRect(brush = bgGradient)

            // Animated light sweep / motion grid
            val currentX = width * animOffset
            drawLine(
                brush = Brush.linearGradient(
                    listOf(Color.Transparent, NaturalLimeAccent.copy(alpha = 0.5f), Color.Transparent)
                ),
                start = Offset(currentX, 0f),
                end = Offset(currentX, height),
                strokeWidth = 3.dp.toPx()
            )

            // Volumetric orbit rings
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(NaturalSagePill.copy(alpha = 0.3f), Color.Transparent),
                    center = Offset(width * 0.5f, height * 0.5f),
                    radius = height * 0.6f
                ),
                center = Offset(width * 0.5f, height * 0.5f),
                radius = height * 0.5f
            )
        }

        // Center Play / Pause Overlay Icon
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(NaturalSurface.copy(alpha = 0.85f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = NaturalPrimary,
                modifier = Modifier.size(28.dp)
            )
        }

        // Corner 4K / 60 FPS Badge
        Surface(
            color = NaturalPrimaryDark.copy(alpha = 0.8f),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            Text(
                text = "$resolution • 60 FPS HDR",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalLimeAccent,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
    }
}
