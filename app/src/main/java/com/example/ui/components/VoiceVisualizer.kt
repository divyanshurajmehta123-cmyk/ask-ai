package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NaturalLimeAccent
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalPrimaryDark
import com.example.ui.theme.NaturalSagePill

@Composable
fun VoiceWaveVisualizer(
    waveAmplitudes: List<Float>,
    isListening: Boolean,
    isSpeaking: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        val count = waveAmplitudes.size
        val barWidth = 12.dp.toPx()
        val spacing = 10.dp.toPx()
        val totalWidth = (count * barWidth) + ((count - 1) * spacing)
        var startX = (size.width - totalWidth) / 2f
        val centerY = size.height / 2f

        val gradient = Brush.verticalGradient(
            colors = listOf(
                NaturalPrimary,
                NaturalLimeAccent,
                NaturalPrimaryDark
            )
        )

        waveAmplitudes.forEachIndexed { _, amp ->
            val scale = if (isListening || isSpeaking) pulseScale else 1.0f
            val barHeight = (size.height * 0.8f * amp.coerceIn(0.1f, 1.0f) * scale).coerceAtLeast(10.dp.toPx())
            val top = centerY - (barHeight / 2f)

            drawRoundRect(
                brush = gradient,
                topLeft = Offset(startX, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
            )

            startX += barWidth + spacing
        }
    }
}

@Composable
fun OrbGlowingVisualizer(
    isListening: Boolean,
    isSpeaking: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb")
    val pulseOrb by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orbPulse"
    )

    Canvas(modifier = modifier.size(180.dp)) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val baseRadius = size.width * 0.35f
        val activeScale = if (isListening || isSpeaking) pulseOrb else 1.0f

        // Outer glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    NaturalLimeAccent.copy(alpha = if (isListening) 0.6f else 0.3f),
                    NaturalSagePill.copy(alpha = 0.2f),
                    Color.Transparent
                ),
                center = center,
                radius = baseRadius * 1.5f * activeScale
            ),
            radius = baseRadius * 1.5f * activeScale,
            center = center
        )

        // Core Orb
        drawCircle(
            brush = Brush.linearGradient(
                colors = listOf(NaturalPrimary, NaturalLimeAccent, NaturalPrimaryDark),
                start = Offset(center.x - baseRadius, center.y - baseRadius),
                end = Offset(center.x + baseRadius, center.y + baseRadius)
            ),
            radius = baseRadius * activeScale,
            center = center
        )

        // Orbital ring
        drawCircle(
            color = NaturalPrimary.copy(alpha = 0.5f),
            radius = baseRadius * 1.25f,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}
