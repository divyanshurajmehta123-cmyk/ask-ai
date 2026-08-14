package com.example.data.model

enum class AppDestination(val title: String) {
    CHAT("Chat"),
    IMAGE_AI("Image AI"),
    VIDEO_AI("Video AI"),
    VOICE_AI("Voice AI"),
    EXPLORE("Explore")
}

enum class TopMenuOption(val title: String) {
    UPGRADE_PREMIUM("Upgrade to Premium"),
    SETTINGS("Settings"),
    HISTORY("History"),
    PROFILE("Profile")
}

enum class AIModelTier(val id: String, val displayName: String, val isProOnly: Boolean, val description: String) {
    GEMINI_FLASH("gemini-3.5-flash", "Gemini 3.5 Flash", false, "Blazing fast everyday assistant & reasoning"),
    GEMINI_PRO("gemini-3.1-pro-preview", "Gemini 3.1 Pro (4K Context)", true, "Advanced STEM, deep coding, and complex logic"),
    GEMINI_IMAGE("gemini-2.5-flash-image", "Gemini 2.5 Image Vision", false, "High-fidelity visual generation & vision reasoning")
}

enum class VideoResolution(val label: String, val isPro: Boolean, val qualityLabel: String) {
    RES_720P("720p HD", false, "Standard Fast Render"),
    RES_1080P("1080p FHD", false, "Full High Definition"),
    RES_4K("4K Ultra HD", true, "Studio 4K Master (Veo Cinema)")
}

data class VideoPreset(
    val id: String,
    val title: String,
    val promptTemplate: String,
    val style: String,
    val duration: String = "5s",
    val motion: String = "Smooth Cinematic"
)

data class ImagePreset(
    val id: String,
    val title: String,
    val styleTag: String,
    val samplePrompt: String
)

data class UserSubscription(
    val isPremium: Boolean = false,
    val planName: String = "Free Starter",
    val price: String = "₹1,999",
    val maxDailyGenerations: Int = 15,
    val usedGenerations: Int = 3,
    val allows4KVideo: Boolean = false,
    val allowsProModel: Boolean = false,
    val expiryDate: String = "Lifetime Access"
)
