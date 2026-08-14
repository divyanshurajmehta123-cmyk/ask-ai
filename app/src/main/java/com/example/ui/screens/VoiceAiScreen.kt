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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OrbGlowingVisualizer
import com.example.ui.components.VoiceWaveVisualizer
import com.example.ui.theme.NaturalBackground
import com.example.ui.theme.NaturalCardBorder
import com.example.ui.theme.NaturalLimeAccent
import com.example.ui.theme.NaturalLimeDarkText
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalPrimaryDark
import com.example.ui.theme.NaturalSagePill
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalSurfaceVariant
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary
import com.example.ui.theme.NaturalTextTertiary

@Composable
fun VoiceAiScreen(
    isListening: Boolean,
    isSpeaking: Boolean,
    transcript: String,
    voiceResponse: String,
    waveAmplitudes: List<Float>,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    onSpeakResponse: (String) -> Unit,
    onStopTts: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NaturalBackground)
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Header Info
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Surface(
                color = if (isListening) NaturalLimeAccent else NaturalSagePill,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = if (isListening) NaturalLimeDarkText else NaturalPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = when {
                            isListening -> "Listening to speech..."
                            isSpeaking -> "Nova AI is speaking..."
                            else -> "Voice Assistant Ready"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isListening) NaturalLimeDarkText else NaturalPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Live Neural Voice",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalTextPrimary
            )

            Text(
                text = "Natural conversational speech synthesis with zero latency",
                fontSize = 12.sp,
                color = NaturalTextSecondary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Glowing Orb Visualizer
        Box(
            modifier = Modifier
                .size(190.dp)
                .clickable {
                    if (isListening) onStopListening() else onStartListening()
                },
            contentAlignment = Alignment.Center
        ) {
            OrbGlowingVisualizer(
                isListening = isListening,
                isSpeaking = isSpeaking
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dynamic Waveform Visualizer Canvas
        VoiceWaveVisualizer(
            waveAmplitudes = waveAmplitudes,
            isListening = isListening,
            isSpeaking = isSpeaking,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Transcript / Response Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = NaturalSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(18.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "User Input",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = transcript,
                    fontSize = 14.sp,
                    color = NaturalTextPrimary,
                    lineHeight = 20.sp
                )

                if (voiceResponse.isNotBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Nova AI Reply",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalPrimary
                        )

                        Row {
                            IconButton(
                                onClick = { onSpeakResponse(voiceResponse) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Speak",
                                    tint = NaturalPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            if (isSpeaking) {
                                IconButton(
                                    onClick = onStopTts,
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Stop,
                                        contentDescription = "Stop",
                                        tint = Color(0xFFDC2626),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = voiceResponse,
                        fontSize = 13.sp,
                        color = NaturalTextSecondary,
                        lineHeight = 19.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Mic Action Button
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(
                    if (isListening)
                        Color(0xFFDC2626)
                    else
                        NaturalPrimary
                )
                .clickable {
                    if (isListening) onStopListening() else onStartListening()
                }
                .testTag("voice_mic_main_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                contentDescription = if (isListening) "Stop Listening" else "Start Talking",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }

        Text(
            text = if (isListening) "Tap to Stop Listening" else "Tap to Speak",
            fontSize = 12.sp,
            color = NaturalTextSecondary,
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
        )
    }
}
