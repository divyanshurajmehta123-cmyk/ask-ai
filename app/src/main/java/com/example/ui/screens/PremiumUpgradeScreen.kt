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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun PremiumUpgradeScreen(
    subscription: UserSubscription,
    onUpgradeConfirmed: () -> Unit,
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()
    var showCheckoutDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NaturalBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Close button on top right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("close_premium_screen")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = NaturalTextPrimary
                    )
                }
            }

            // Hero Botanical Badge
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(NaturalPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = "VIP Premium",
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Upgrade to Nova Pro",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalTextPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Unlock AI video generation up to 4K quality, priority GPU compute, larger context, and advanced models",
                fontSize = 13.sp,
                color = NaturalTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Pricing Banner Card (Botanical Green card with Lime button as per Natural Tones design)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalPrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        color = NaturalLimeAccent,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "LIFETIME VIP PASS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NaturalLimeDarkText,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "₹1,999",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Text(
                        text = "One-time payment • No recurring subscriptions",
                        fontSize = 12.sp,
                        color = NaturalSagePill,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!subscription.isPremium) {
                        Button(
                            onClick = { showCheckoutDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_upgrade_now"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NaturalLimeAccent,
                                contentColor = NaturalLimeDarkText
                            )
                        ) {
                            Text(
                                text = "Get Nova Pro (₹1,999)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Surface(
                            color = NaturalLimeAccent,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = NaturalLimeDarkText,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Active VIP Member",
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalLimeDarkText,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Feature Checklist Cards
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureRow(
                    icon = Icons.Default.HighQuality,
                    iconTint = NaturalPrimary,
                    title = "AI Video Generation up to 4K Quality",
                    subtitle = "Render cinematic Veo videos in ultra-sharp 4K resolution at 60 FPS."
                )

                FeatureRow(
                    icon = Icons.Default.Speed,
                    iconTint = NaturalPrimary,
                    title = "Faster Responses & Priority Processing",
                    subtitle = "Skip standard queues with dedicated high-speed neural compute clusters."
                )

                FeatureRow(
                    icon = Icons.Default.AutoAwesome,
                    iconTint = NaturalPrimary,
                    title = "Advanced AI Models (Gemini 3.1 Pro)",
                    subtitle = "Unlock 1M+ token context window, deep STEM coding, and complex logic."
                )

                FeatureRow(
                    icon = Icons.Default.FlashOn,
                    iconTint = NaturalPrimary,
                    title = "Unlimited High-Resolution Studio",
                    subtitle = "Generate endless photorealistic images with zero daily caps."
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Secured by Google Play Billing • Instant Activation",
                fontSize = 11.sp,
                color = NaturalTextTertiary
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Checkout Dialog Simulation
        if (showCheckoutDialog) {
            AlertDialog(
                onDismissRequest = { showCheckoutDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = NaturalPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Confirm Upgrade", color = NaturalTextPrimary, fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Nova AI VIP Pro Lifetime Subscription",
                            fontWeight = FontWeight.SemiBold,
                            color = NaturalTextPrimary
                        )
                        Text(
                            text = "Price: ₹1,999",
                            fontWeight = FontWeight.Bold,
                            color = NaturalPrimary,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Includes 4K Veo AI Video generation, Gemini 3.1 Pro models, priority response queue, and unlimited creations.",
                            color = NaturalTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showCheckoutDialog = false
                            onUpgradeConfirmed()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NaturalPrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("confirm_purchase_btn")
                    ) {
                        Text("Pay ₹1,999 & Activate", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showCheckoutDialog = false }) {
                        Text("Cancel", color = NaturalTextSecondary)
                    }
                },
                containerColor = NaturalSurface
            )
        }
    }
}

@Composable
fun FeatureRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = NaturalSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(NaturalSagePill),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = NaturalTextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = NaturalTextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
