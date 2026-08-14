package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.BuildConfig
import com.example.data.model.AIModelTier
import com.example.data.model.UserSubscription
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
fun SettingsScreen(
    subscription: UserSubscription,
    activeModelTier: AIModelTier,
    temperature: Float,
    systemInstruction: String,
    onModelSelected: (AIModelTier) -> Unit,
    onTemperatureChanged: (Float) -> Unit,
    onSystemInstructionChanged: (String) -> Unit,
    onToggleProTest: () -> Unit,
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var localTemp by remember { mutableFloatStateOf(temperature) }
    var localInstruction by remember { mutableStateOf(systemInstruction) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NaturalBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = NaturalPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Preferences & AI Engine",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier.testTag("close_settings_btn")
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = NaturalTextPrimary)
            }
        }

        // Model Architecture
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NaturalSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Active AI Neural Model",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ModelChoiceCard(
                        title = "Gemini 3.5 Flash",
                        desc = "Ultra low latency • High speed",
                        isSelected = activeModelTier == AIModelTier.GEMINI_FLASH,
                        onClick = { onModelSelected(AIModelTier.GEMINI_FLASH) },
                        modifier = Modifier.weight(1f)
                    )

                    ModelChoiceCard(
                        title = "Gemini 3.1 Pro 👑",
                        desc = "1M+ context • Deep STEM logic",
                        isSelected = activeModelTier == AIModelTier.GEMINI_PRO,
                        onClick = { onModelSelected(AIModelTier.GEMINI_PRO) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Creativity Temperature Slider
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NaturalSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Creativity Temperature",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextPrimary
                    )
                    Text(
                        text = String.format("%.2f", localTemp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalPrimary
                    )
                }

                Slider(
                    value = localTemp,
                    onValueChange = {
                        localTemp = it
                        onTemperatureChanged(it)
                    },
                    valueRange = 0.0f..1.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = NaturalPrimary,
                        activeTrackColor = NaturalPrimary,
                        inactiveTrackColor = NaturalSagePill
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Precise & Deterministic", fontSize = 11.sp, color = NaturalTextSecondary)
                    Text(text = "Creative & Imaginative", fontSize = 11.sp, color = NaturalTextSecondary)
                }
            }
        }

        // Custom Persona / System Instruction
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NaturalSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Custom System Persona",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )

                OutlinedTextField(
                    value = localInstruction,
                    onValueChange = {
                        localInstruction = it
                        onSystemInstructionChanged(it)
                    },
                    placeholder = { Text("Instruct Nova AI on behavior, tone, and role...", color = NaturalTextTertiary) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = NaturalTextPrimary,
                        unfocusedTextColor = NaturalTextPrimary,
                        focusedBorderColor = NaturalPrimary,
                        unfocusedBorderColor = NaturalCardBorder,
                        focusedContainerColor = NaturalSurfaceVariant,
                        unfocusedContainerColor = NaturalSurfaceVariant
                    )
                )
            }
        }

        // Developer & Subscription Mode Toggle
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NaturalSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Subscription & Testing Affordance",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (subscription.isPremium) "VIP Pro Mode (Active)" else "Free Starter Mode (Active)",
                            fontWeight = FontWeight.SemiBold,
                            color = if (subscription.isPremium) NaturalPrimary else NaturalTextPrimary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Toggle to preview Free vs. ₹1,999 VIP tier perks",
                            fontSize = 11.sp,
                            color = NaturalTextSecondary
                        )
                    }

                    Switch(
                        checked = subscription.isPremium,
                        onCheckedChange = { onToggleProTest() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NaturalPrimary,
                            checkedTrackColor = NaturalSagePill
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ModelChoiceCard(
    title: String,
    desc: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) NaturalSagePill else NaturalSurfaceVariant,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .border(
                1.dp,
                if (isSelected) NaturalPrimary else NaturalCardBorder.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) NaturalPrimary else NaturalTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                fontSize = 10.sp,
                color = NaturalTextSecondary
            )
        }
    }
}
