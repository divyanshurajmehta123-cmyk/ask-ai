package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AppDestination
import com.example.data.model.TopMenuOption
import com.example.ui.components.NovaBottomBar
import com.example.ui.components.NovaTopBar
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.ImageAiScreen
import com.example.ui.screens.PremiumUpgradeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.VideoAiScreen
import com.example.ui.screens.VoiceAiScreen
import com.example.ui.theme.NaturalBackground
import com.example.ui.viewmodel.NovaAiViewModel

@Composable
fun NovaAiApp(viewModel: NovaAiViewModel = viewModel()) {
    val currentDestination by viewModel.currentDestination.collectAsState()
    val activeOverlay by viewModel.activeTopMenuOverlay.collectAsState()
    val subscription by viewModel.subscription.collectAsState()

    // Chat states
    val messages by viewModel.messages.collectAsState()
    val chatInput by viewModel.chatInput.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val activeModelTier by viewModel.activeModelTier.collectAsState()
    val temperature by viewModel.temperature.collectAsState()
    val systemInstruction by viewModel.customSystemInstruction.collectAsState()

    // Image AI states
    val imagePrompt by viewModel.imagePrompt.collectAsState()
    val selectedImageStyle by viewModel.selectedImageStyle.collectAsState()
    val selectedAspectRatio by viewModel.selectedAspectRatio.collectAsState()
    val isGeneratingImage by viewModel.isGeneratingImage.collectAsState()
    val latestGeneratedImage by viewModel.latestGeneratedImage.collectAsState()

    // Video AI states
    val videoPrompt by viewModel.videoPrompt.collectAsState()
    val selectedResolution by viewModel.selectedResolution.collectAsState()
    val selectedCameraStyle by viewModel.selectedCameraStyle.collectAsState()
    val selectedDuration by viewModel.selectedDuration.collectAsState()
    val isRenderingVideo by viewModel.isRenderingVideo.collectAsState()
    val videoRenderProgress by viewModel.videoRenderProgress.collectAsState()
    val videoRenderStage by viewModel.videoRenderStage.collectAsState()
    val latestRenderedVideo by viewModel.latestRenderedVideo.collectAsState()

    // Voice AI states
    val isListening by viewModel.isListening.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val voiceTranscript by viewModel.voiceTranscript.collectAsState()
    val voiceResponse by viewModel.voiceResponse.collectAsState()
    val audioWaveLevels by viewModel.audioWaveLevels.collectAsState()

    // Explore / Hub states
    val exploreTab by viewModel.exploreTab.collectAsState()
    val codePrompt by viewModel.codePrompt.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val generatedCodeResult by viewModel.generatedCodeResult.collectAsState()
    val isGeneratingCode by viewModel.isGeneratingCode.collectAsState()

    val translateSourceText by viewModel.translateSourceText.collectAsState()
    val targetLanguage by viewModel.targetLanguage.collectAsState()
    val translatedResult by viewModel.translatedResult.collectAsState()
    val isTranslating by viewModel.isTranslating.collectAsState()

    val documentText by viewModel.documentText.collectAsState()
    val documentAnalysisResult by viewModel.documentAnalysisResult.collectAsState()
    val isAnalyzingDoc by viewModel.isAnalyzingDoc.collectAsState()

    // History / Saved
    val allCreations by viewModel.allCreations.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(NaturalBackground)) {
        Scaffold(
            topBar = {
                if (activeOverlay == null) {
                    NovaTopBar(
                        currentDestination = currentDestination,
                        subscription = subscription,
                        onMenuOptionSelected = { viewModel.openTopMenuOverlay(it) },
                        onUpgradeClicked = { viewModel.openTopMenuOverlay(TopMenuOption.UPGRADE_PREMIUM) }
                    )
                }
            },
            bottomBar = {
                if (activeOverlay == null) {
                    NovaBottomBar(
                        currentDestination = currentDestination,
                        onDestinationSelected = { viewModel.navigateTo(it) }
                    )
                }
            },
            containerColor = NaturalBackground
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // If overlay is active, show the overlay screen
                if (activeOverlay != null) {
                    when (activeOverlay) {
                        TopMenuOption.UPGRADE_PREMIUM -> {
                            PremiumUpgradeScreen(
                                subscription = subscription,
                                onUpgradeConfirmed = { viewModel.upgradeToPremium() },
                                onClose = { viewModel.closeTopMenuOverlay() }
                            )
                        }
                        TopMenuOption.SETTINGS -> {
                            SettingsScreen(
                                subscription = subscription,
                                activeModelTier = activeModelTier,
                                temperature = temperature,
                                systemInstruction = systemInstruction,
                                onModelSelected = { viewModel.setModelTier(it) },
                                onTemperatureChanged = { viewModel.setTemperature(it) },
                                onSystemInstructionChanged = { viewModel.setCustomSystemInstruction(it) },
                                onToggleProTest = { viewModel.toggleSubscriptionForTesting() },
                                onClose = { viewModel.closeTopMenuOverlay() }
                            )
                        }
                        TopMenuOption.HISTORY -> {
                            HistoryScreen(
                                creations = allCreations,
                                onDeleteCreation = { viewModel.deleteCreation(it) },
                                onToggleFavorite = { viewModel.toggleFavoriteCreation(it) },
                                onClose = { viewModel.closeTopMenuOverlay() }
                            )
                        }
                        TopMenuOption.PROFILE -> {
                            ProfileScreen(
                                subscription = subscription,
                                onUpgradeClicked = { viewModel.openTopMenuOverlay(TopMenuOption.UPGRADE_PREMIUM) },
                                onClearAllData = { viewModel.clearChat() },
                                onClose = { viewModel.closeTopMenuOverlay() }
                            )
                        }
                        null -> {}
                    }
                } else {
                    // Main tab navigation
                    AnimatedContent(
                        targetState = currentDestination,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "tab_switch"
                    ) { destination ->
                        when (destination) {
                            AppDestination.CHAT -> {
                                ChatScreen(
                                    messages = messages,
                                    inputText = chatInput,
                                    isGenerating = isGenerating,
                                    activeModelTier = activeModelTier,
                                    subscription = subscription,
                                    onInputChanged = { viewModel.updateChatInput(it) },
                                    onSendMessage = { viewModel.sendMessage(it) },
                                    onModelSelected = { viewModel.setModelTier(it) },
                                    onClearChat = { viewModel.clearChat() },
                                    onDeleteMessage = { viewModel.deleteMessage(it) },
                                    onSpeakMessage = { viewModel.speakOut(it) },
                                    onOpenVoiceMode = { viewModel.navigateTo(AppDestination.VOICE_AI) }
                                )
                            }
                            AppDestination.IMAGE_AI -> {
                                ImageAiScreen(
                                    prompt = imagePrompt,
                                    selectedStyle = selectedImageStyle,
                                    selectedAspectRatio = selectedAspectRatio,
                                    isGenerating = isGeneratingImage,
                                    latestImage = latestGeneratedImage,
                                    presets = viewModel.imagePresets,
                                    onPromptChanged = { viewModel.updateImagePrompt(it) },
                                    onStyleSelected = { viewModel.setImageStyle(it) },
                                    onAspectRatioSelected = { viewModel.setAspectRatio(it) },
                                    onGenerateClicked = { viewModel.generateImage(it) }
                                )
                            }
                            AppDestination.VIDEO_AI -> {
                                VideoAiScreen(
                                    prompt = videoPrompt,
                                    selectedResolution = selectedResolution,
                                    selectedCameraStyle = selectedCameraStyle,
                                    selectedDuration = selectedDuration,
                                    isRendering = isRenderingVideo,
                                    renderProgress = videoRenderProgress,
                                    renderStage = videoRenderStage,
                                    latestVideo = latestRenderedVideo,
                                    subscription = subscription,
                                    presets = viewModel.videoPresets,
                                    onPromptChanged = { viewModel.updateVideoPrompt(it) },
                                    onResolutionSelected = { viewModel.setVideoResolution(it) },
                                    onCameraStyleSelected = { viewModel.setCameraStyle(it) },
                                    onDurationSelected = { viewModel.setVideoDuration(it) },
                                    onGenerateClicked = { viewModel.generateVideo(it) },
                                    onOpenUpgradeModal = { viewModel.openTopMenuOverlay(TopMenuOption.UPGRADE_PREMIUM) }
                                )
                            }
                            AppDestination.VOICE_AI -> {
                                VoiceAiScreen(
                                    isListening = isListening,
                                    isSpeaking = isSpeaking,
                                    transcript = voiceTranscript,
                                    voiceResponse = voiceResponse,
                                    waveAmplitudes = audioWaveLevels,
                                    onStartListening = { viewModel.startVoiceListening() },
                                    onStopListening = { viewModel.stopVoiceListening() },
                                    onSpeakResponse = { viewModel.speakOut(it) },
                                    onStopTts = { viewModel.stopTts() }
                                )
                            }
                            AppDestination.EXPLORE -> {
                                ExploreScreen(
                                    currentTab = exploreTab,
                                    codePrompt = codePrompt,
                                    selectedLanguage = selectedLanguage,
                                    codeResult = generatedCodeResult,
                                    isGeneratingCode = isGeneratingCode,
                                    onCodePromptChanged = { viewModel.updateCodePrompt(it) },
                                    onCodeLanguageSelected = { viewModel.setCodeLanguage(it) },
                                    onGenerateCode = { viewModel.generateCode() },
                                    sourceText = translateSourceText,
                                    targetLanguage = targetLanguage,
                                    translatedResult = translatedResult,
                                    isTranslating = isTranslating,
                                    onSourceTextChanged = { viewModel.updateTranslateSourceText(it) },
                                    onTargetLanguageSelected = { viewModel.setTargetLanguage(it) },
                                    onTranslate = { viewModel.performTranslation() },
                                    documentText = documentText,
                                    documentResult = documentAnalysisResult,
                                    isAnalyzingDoc = isAnalyzingDoc,
                                    onDocumentTextChanged = { viewModel.updateDocumentText(it) },
                                    onAnalyzeDoc = { viewModel.analyzeDocument(it) },
                                    onTabSelected = { viewModel.setExploreTab(it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
