package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppDestination
import com.example.ui.theme.NaturalNavBackground
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalSagePill
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary

sealed class BottomNavItem(
    val destination: AppDestination,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    object Chat : BottomNavItem(AppDestination.CHAT, Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline, "tab_chat")
    object ImageAi : BottomNavItem(AppDestination.IMAGE_AI, Icons.Filled.Image, Icons.Outlined.Image, "tab_image_ai")
    object VideoAi : BottomNavItem(AppDestination.VIDEO_AI, Icons.Filled.Videocam, Icons.Outlined.Videocam, "tab_video_ai")
    object VoiceAi : BottomNavItem(AppDestination.VOICE_AI, Icons.Filled.Mic, Icons.Outlined.Mic, "tab_voice_ai")
    object Explore : BottomNavItem(AppDestination.EXPLORE, Icons.Filled.Explore, Icons.Outlined.Explore, "tab_explore")
}

@Composable
fun NovaBottomBar(
    currentDestination: AppDestination,
    onDestinationSelected: (AppDestination) -> Unit
) {
    val items = listOf(
        BottomNavItem.Chat,
        BottomNavItem.ImageAi,
        BottomNavItem.VideoAi,
        BottomNavItem.VoiceAi,
        BottomNavItem.Explore
    )

    NavigationBar(
        modifier = Modifier
            .background(NaturalNavBackground)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = NaturalNavBackground,
        tonalElevation = 2.dp
    ) {
        items.forEach { item ->
            val isSelected = currentDestination == item.destination
            NavigationBarItem(
                modifier = Modifier.testTag(item.testTag),
                selected = isSelected,
                onClick = { onDestinationSelected(item.destination) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.destination.title,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.destination.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NaturalPrimary,
                    selectedTextColor = NaturalTextPrimary,
                    indicatorColor = NaturalSagePill,
                    unselectedIconColor = NaturalTextSecondary.copy(alpha = 0.7f),
                    unselectedTextColor = NaturalTextSecondary.copy(alpha = 0.7f)
                )
            )
        }
    }
}
