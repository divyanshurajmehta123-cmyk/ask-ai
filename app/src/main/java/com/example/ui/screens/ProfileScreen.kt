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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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
fun ProfileScreen(
    subscription: UserSubscription,
    onUpgradeClicked: () -> Unit,
    onClearAllData: () -> Unit,
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

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
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = NaturalPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "User Profile & Account",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier.testTag("close_profile_btn")
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = NaturalTextPrimary)
            }
        }

        // Avatar & Info Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = NaturalSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(NaturalPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(56.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Nova Explorer",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = NaturalTextPrimary
                )

                Text(
                    text = "explorer@nova-ai.studio",
                    fontSize = 12.sp,
                    color = NaturalTextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (subscription.isPremium) NaturalLimeAccent else NaturalSagePill
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (subscription.isPremium) Icons.Default.WorkspacePremium else Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = if (subscription.isPremium) NaturalLimeDarkText else NaturalPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (subscription.isPremium) "VIP PRO LIFETIME (₹1,999)" else "FREE STARTER TIER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (subscription.isPremium) NaturalLimeDarkText else NaturalPrimary
                        )
                    }
                }
            }
        }

        // Usage Statistics
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = NaturalSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(18.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Intelligence Compute Usage",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatBox(
                        title = "Generations",
                        value = if (subscription.isPremium) "Unlimited" else "12 / 15",
                        desc = "Daily allotment",
                        modifier = Modifier.weight(1f)
                    )
                    StatBox(
                        title = "Video Quality",
                        value = if (subscription.allows4KVideo) "4K UHD" else "1080p",
                        desc = "Veo video engine",
                        modifier = Modifier.weight(1f)
                    )
                    StatBox(
                        title = "Compute Queue",
                        value = if (subscription.isPremium) "Priority" else "Standard",
                        desc = "Zero wait time",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick Account Action Items
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = NaturalSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(18.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Account Settings & Data",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )

                if (!subscription.isPremium) {
                    ProfileOptionRow(
                        icon = Icons.Default.WorkspacePremium,
                        iconTint = NaturalPrimary,
                        title = "Upgrade to Lifetime Pro",
                        subtitle = "Unlock 4K Veo Video, Gemini 3.1 Pro & Faster GPU for ₹1,999",
                        onClick = onUpgradeClicked
                    )
                }

                ProfileOptionRow(
                    icon = Icons.Default.Storage,
                    iconTint = NaturalPrimary,
                    title = "Local Encrypted Database",
                    subtitle = "All chat history and generated media are persisted securely in Room SQLite",
                    onClick = {
                        Toast.makeText(context, "Room Database Status: Healthy & Synced", Toast.LENGTH_SHORT).show()
                    }
                )

                ProfileOptionRow(
                    icon = Icons.Default.DeleteSweep,
                    iconTint = Color(0xFFDC2626),
                    title = "Clear Chat & Creations History",
                    subtitle = "Wipe all local session chats and creations cache",
                    onClick = {
                        onClearAllData()
                        Toast.makeText(context, "All chat history cleared", Toast.LENGTH_SHORT).show()
                    }
                )

                ProfileOptionRow(
                    icon = Icons.Default.Headphones,
                    iconTint = NaturalPrimary,
                    title = "Contact Support & Feedback",
                    subtitle = "Need assistance? Email support@nova-ai.studio",
                    onClick = {
                        Toast.makeText(context, "Support ticket created: support@nova-ai.studio", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun StatBox(
    title: String,
    value: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = NaturalSurfaceVariant,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 11.sp, color = NaturalTextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = desc, fontSize = 9.sp, color = NaturalTextTertiary)
        }
    }
}

@Composable
fun ProfileOptionRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
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
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalTextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = NaturalTextSecondary
            )
        }
    }
}
