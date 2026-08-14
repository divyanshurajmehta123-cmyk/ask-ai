package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppDestination
import com.example.data.model.TopMenuOption
import com.example.data.model.UserSubscription
import com.example.ui.theme.NaturalBackground
import com.example.ui.theme.NaturalCardBorder
import com.example.ui.theme.NaturalLimeAccent
import com.example.ui.theme.NaturalLimeDarkText
import com.example.ui.theme.NaturalNavBackground
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalSagePill
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalSurfaceVariant
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaTopBar(
    currentDestination: AppDestination,
    subscription: UserSubscription,
    onMenuOptionSelected: (TopMenuOption) -> Unit,
    onUpgradeClicked: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = NaturalBackground,
            titleContentColor = NaturalTextPrimary
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botanical Green Avatar Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(NaturalPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Nova AI Logo",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Nova AI",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = NaturalTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        if (subscription.isPremium) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NaturalLimeAccent)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "PRO VIP",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = NaturalLimeDarkText
                                )
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(NaturalPrimary)
                        )
                        Text(
                            text = currentDestination.title + " • Online",
                            fontSize = 11.sp,
                            color = NaturalTextSecondary,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        },
        actions = {
            // Upgrade pill button if user is not premium
            if (!subscription.isPremium) {
                Surface(
                    modifier = Modifier
                        .testTag("upgrade_top_pill")
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onUpgradeClicked() },
                    color = NaturalSagePill,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "Upgrade",
                            tint = NaturalPrimary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "PRO ₹1,999",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
            }

            // Top-right 3-dots Menu Button
            Box {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(NaturalSurfaceVariant)
                        .clickable { menuExpanded = true }
                        .testTag("top_menu_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options Menu",
                        tint = NaturalTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier
                        .background(NaturalSurface)
                        .clip(RoundedCornerShape(14.dp))
                ) {
                    // Option 1: Upgrade to Premium
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = NaturalPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Upgrade to Premium",
                                        fontWeight = FontWeight.SemiBold,
                                        color = NaturalPrimary,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "₹1,999 • 4K Video & Pro AI",
                                        fontSize = 11.sp,
                                        color = NaturalTextSecondary
                                    )
                                }
                            }
                        },
                        onClick = {
                            menuExpanded = false
                            onMenuOptionSelected(TopMenuOption.UPGRADE_PREMIUM)
                        },
                        modifier = Modifier.testTag("menu_upgrade_premium")
                    )

                    HorizontalDivider(color = NaturalCardBorder.copy(alpha = 0.5f))

                    // Option 2: Settings
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = NaturalTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Settings",
                                    color = NaturalTextPrimary,
                                    fontSize = 14.sp
                                )
                            }
                        },
                        onClick = {
                            menuExpanded = false
                            onMenuOptionSelected(TopMenuOption.SETTINGS)
                        },
                        modifier = Modifier.testTag("menu_settings")
                    )

                    // Option 3: History
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = NaturalTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "History",
                                    color = NaturalTextPrimary,
                                    fontSize = 14.sp
                                )
                            }
                        },
                        onClick = {
                            menuExpanded = false
                            onMenuOptionSelected(TopMenuOption.HISTORY)
                        },
                        modifier = Modifier.testTag("menu_history")
                    )

                    // Option 4: Profile
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = NaturalTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Profile",
                                    color = NaturalTextPrimary,
                                    fontSize = 14.sp
                                )
                            }
                        },
                        onClick = {
                            menuExpanded = false
                            onMenuOptionSelected(TopMenuOption.PROFILE)
                        },
                        modifier = Modifier.testTag("menu_profile")
                    )
                }
            }
        }
    )
}
