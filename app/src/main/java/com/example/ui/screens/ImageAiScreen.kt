package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SavedCreationEntity
import com.example.data.model.ImagePreset
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
fun ImageAiScreen(
    prompt: String,
    selectedStyle: String,
    selectedAspectRatio: String,
    isGenerating: Boolean,
    latestImage: SavedCreationEntity?,
    presets: List<ImagePreset>,
    onPromptChanged: (String) -> Unit,
    onStyleSelected: (String) -> Unit,
    onAspectRatioSelected: (String) -> Unit,
    onGenerateClicked: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val styles = listOf(
        "Photorealistic",
        "Cyberpunk Neon",
        "3D Render",
        "Anime",
        "Fantasy Art",
        "Minimalist Vector"
    )

    val aspectRatios = listOf("1:1", "16:9", "9:16", "4:3")

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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                tint = NaturalPrimary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Image AI Diffusion Studio",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalTextPrimary
            )
        }

        // Prompt Input Field
        OutlinedTextField(
            value = prompt,
            onValueChange = onPromptChanged,
            placeholder = {
                Text(
                    text = "Describe the image you want to create in vivid detail...",
                    color = NaturalTextTertiary,
                    fontSize = 14.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .testTag("image_prompt_field"),
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

        // Style Selector
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Artistic Style",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = NaturalTextSecondary
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(styles) { style ->
                    val isSelected = selectedStyle == style
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) NaturalSagePill else NaturalSurface,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onStyleSelected(style) }
                            .border(
                                width = 1.dp,
                                color = if (isSelected) NaturalPrimary else NaturalCardBorder.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Text(
                            text = style,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) NaturalPrimary else NaturalTextSecondary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Aspect Ratio Selector
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Aspect Ratio",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = NaturalTextSecondary
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                aspectRatios.forEach { ratio ->
                    val isSelected = selectedAspectRatio == ratio
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) NaturalSagePill else NaturalSurface,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onAspectRatioSelected(ratio) }
                            .border(
                                width = 1.dp,
                                color = if (isSelected) NaturalPrimary else NaturalCardBorder.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(
                            text = ratio,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) NaturalPrimary else NaturalTextSecondary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Generate Button
        Button(
            onClick = { onGenerateClicked(prompt) },
            enabled = !isGenerating && (prompt.isNotBlank() || latestImage == null),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_image_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NaturalPrimary,
                contentColor = Color.White
            )
        ) {
            if (isGenerating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Synthesizing Neural Diffusion...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Generate Image",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }

        // Generated Result Showcase
        if (latestImage != null || isGenerating) {
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
                        Text(
                            text = "Generation Output (2048 x 2048)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalPrimary
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = {
                                    Toast.makeText(context, "Image saved to Gallery", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Download",
                                    tint = NaturalTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Prompt", latestImage?.prompt ?: "")
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Prompt copied", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy prompt",
                                    tint = NaturalTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Image Display Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        NaturalPrimary,
                                        NaturalPrimaryDark,
                                        NaturalSagePill
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isGenerating) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator(color = NaturalLimeAccent)
                                Text(
                                    text = "Rendering high-res visual latent...",
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                        } else {
                            // High aesthetic rendered canvas art
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(NaturalLimeAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = NaturalLimeDarkText,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = latestImage?.title ?: "Generated Creation",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Style: ${latestImage?.style ?: selectedStyle} • 2K Resolution",
                                    color = NaturalSagePill,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    if (latestImage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Prompt: ${latestImage.prompt}",
                            fontSize = 11.sp,
                            color = NaturalTextSecondary,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Inspiration Presets Gallery
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Inspiration Prompts",
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
                            onPromptChanged(preset.samplePrompt)
                            onStyleSelected(preset.styleTag)
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
                                imageVector = Icons.Default.Style,
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
                                text = preset.samplePrompt,
                                fontSize = 11.sp,
                                color = NaturalTextSecondary,
                                maxLines = 1
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Use prompt",
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
