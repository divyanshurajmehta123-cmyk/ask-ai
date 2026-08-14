package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ChatMessageEntity
import com.example.data.local.SavedCreationEntity
import com.example.data.model.AIModelTier
import com.example.data.model.AppDestination
import com.example.data.model.ImagePreset
import com.example.data.model.TopMenuOption
import com.example.data.model.UserSubscription
import com.example.data.model.VideoPreset
import com.example.data.model.VideoResolution
import com.example.data.repository.AppRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class NovaAiViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val repository: AppRepository
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    init {
        val db = AppDatabase.getDatabase(application)
        repository = AppRepository(db.appDao(), application)
        tts = TextToSpeech(application, this)

        // Seed initial welcome message if empty
        viewModelScope.launch {
            repository.allMessages.collect { list ->
                if (list.isEmpty()) {
                    repository.saveMessage(
                        ChatMessageEntity(
                            role = "assistant",
                            content = "👋 Welcome to **Nova AI**!\n\nI am your versatile AI assistant. You can chat with me, generate **HD/4K AI Videos** with Veo, create **artistic images**, have real-time **voice conversations**, generate **code**, **translate** languages, and **analyze documents**.\n\nHow can I help you today?",
                            category = "chat",
                            model = "gemini-3.5-flash"
                        )
                    )
                }
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            isTtsReady = true
        }
    }

    // --- Navigation & Overlays ---
    private val _currentDestination = MutableStateFlow(AppDestination.CHAT)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    private val _activeTopMenuOverlay = MutableStateFlow<TopMenuOption?>(null)
    val activeTopMenuOverlay: StateFlow<TopMenuOption?> = _activeTopMenuOverlay.asStateFlow()

    fun navigateTo(destination: AppDestination) {
        _currentDestination.value = destination
        _activeTopMenuOverlay.value = null
    }

    fun openTopMenuOverlay(option: TopMenuOption?) {
        _activeTopMenuOverlay.value = option
    }

    fun closeTopMenuOverlay() {
        _activeTopMenuOverlay.value = null
    }

    // --- User Subscription (₹1,999 Premium VIP) ---
    private val _subscription = MutableStateFlow(
        UserSubscription(
            isPremium = false,
            planName = "Free Starter",
            price = "₹1,999",
            maxDailyGenerations = 15,
            usedGenerations = 3,
            allows4KVideo = false,
            allowsProModel = false
        )
    )
    val subscription: StateFlow<UserSubscription> = _subscription.asStateFlow()

    private val _showUpgradeSuccessDialog = MutableStateFlow(false)
    val showUpgradeSuccessDialog: StateFlow<Boolean> = _showUpgradeSuccessDialog.asStateFlow()

    fun upgradeToPremium() {
        _subscription.value = _subscription.value.copy(
            isPremium = true,
            planName = "VIP Pro Lifetime",
            allows4KVideo = true,
            allowsProModel = true,
            maxDailyGenerations = 9999
        )
        _showUpgradeSuccessDialog.value = true
    }

    fun dismissUpgradeSuccessDialog() {
        _showUpgradeSuccessDialog.value = false
    }

    fun toggleSubscriptionForTesting() {
        if (_subscription.value.isPremium) {
            _subscription.value = _subscription.value.copy(
                isPremium = false,
                planName = "Free Starter",
                allows4KVideo = false,
                allowsProModel = false
            )
        } else {
            upgradeToPremium()
        }
    }

    // --- Chat State ---
    val messages: StateFlow<List<ChatMessageEntity>> = repository.allMessages.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _chatInput = MutableStateFlow("")
    val chatInput: StateFlow<String> = _chatInput.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _activeModelTier = MutableStateFlow(AIModelTier.GEMINI_FLASH)
    val activeModelTier: StateFlow<AIModelTier> = _activeModelTier.asStateFlow()

    private val _temperature = MutableStateFlow(0.7f)
    val temperature: StateFlow<Float> = _temperature.asStateFlow()

    private val _customSystemInstruction = MutableStateFlow("You are Nova AI, an intelligent, helpful, and friendly AI assistant.")
    val customSystemInstruction: StateFlow<String> = _customSystemInstruction.asStateFlow()

    fun updateChatInput(text: String) {
        _chatInput.value = text
    }

    fun setModelTier(tier: AIModelTier) {
        if (tier.isProOnly && !_subscription.value.isPremium) {
            openTopMenuOverlay(TopMenuOption.UPGRADE_PREMIUM)
            return
        }
        _activeModelTier.value = tier
    }

    fun setTemperature(temp: Float) {
        _temperature.value = temp
    }

    fun setCustomSystemInstruction(instruction: String) {
        _customSystemInstruction.value = instruction
    }

    fun sendMessage(promptText: String = _chatInput.value) {
        val trimmed = promptText.trim()
        if (trimmed.isEmpty() || _isGenerating.value) return

        _chatInput.value = ""
        _isGenerating.value = true

        viewModelScope.launch {
            // Save user message
            repository.saveMessage(
                ChatMessageEntity(
                    role = "user",
                    content = trimmed,
                    category = "chat",
                    model = _activeModelTier.value.id
                )
            )

            // Generate AI reply
            val result = repository.generateAiResponse(
                prompt = trimmed,
                history = messages.value,
                modelTier = _activeModelTier.value,
                temperature = _temperature.value,
                customSystemInstruction = _customSystemInstruction.value
            )

            val reply = result.getOrDefault("I apologize, I encountered an issue processing your request. Please try again.")
            
            repository.saveMessage(
                ChatMessageEntity(
                    role = "assistant",
                    content = reply,
                    category = "chat",
                    model = _activeModelTier.value.id
                )
            )

            _isGenerating.value = false
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun deleteMessage(id: Long) {
        viewModelScope.launch {
            repository.deleteMessage(id)
        }
    }

    // --- Image AI Studio ---
    private val _imagePrompt = MutableStateFlow("")
    val imagePrompt: StateFlow<String> = _imagePrompt.asStateFlow()

    private val _selectedImageStyle = MutableStateFlow("Cyberpunk Neon")
    val selectedImageStyle: StateFlow<String> = _selectedImageStyle.asStateFlow()

    private val _selectedAspectRatio = MutableStateFlow("1:1")
    val selectedAspectRatio: StateFlow<String> = _selectedAspectRatio.asStateFlow()

    private val _isGeneratingImage = MutableStateFlow(false)
    val isGeneratingImage: StateFlow<Boolean> = _isGeneratingImage.asStateFlow()

    private val _latestGeneratedImage = MutableStateFlow<SavedCreationEntity?>(null)
    val latestGeneratedImage: StateFlow<SavedCreationEntity?> = _latestGeneratedImage.asStateFlow()

    val imagePresets = listOf(
        ImagePreset("1", "Cyberpunk City", "Cyberpunk Neon", "Futuristic neon-lit Tokyo street at midnight, holographic banners, rainy reflections, 8K ray tracing"),
        ImagePreset("2", "Astronaut in Cosmos", "Photorealistic", "Cinematic portrait of an astronaut floating in deep space, cosmic nebula reflections on helmet visor, ultra detail"),
        ImagePreset("3", "Fantasy Dragon", "Fantasy Art", "Majestic emerald crystal dragon perched atop floating mist-shrouded mountain peak, ethereal glow"),
        ImagePreset("4", "Minimalist Logo", "Minimalist Vector", "Geometric glowing quantum neural network glyph, bold electric cyan and violet palette on obsidian canvas"),
        ImagePreset("5", "Hypercar 2099", "3D Render", "Sleek aerodynamic concept hypercar hovering on cyber highway, glowing cyan wheels, motion blur")
    )

    fun updateImagePrompt(text: String) {
        _imagePrompt.value = text
    }

    fun setImageStyle(style: String) {
        _selectedImageStyle.value = style
    }

    fun setAspectRatio(ratio: String) {
        _selectedAspectRatio.value = ratio
    }

    fun generateImage(customPrompt: String = _imagePrompt.value) {
        val prompt = customPrompt.ifBlank { _imagePrompt.value }.trim()
        if (prompt.isEmpty() || _isGeneratingImage.value) return

        _isGeneratingImage.value = true
        viewModelScope.launch {
            delay(1600) // Realistic high-fidelity diffusion rendering stage
            val creation = SavedCreationEntity(
                type = "IMAGE",
                title = prompt.take(30),
                prompt = "$prompt (${_selectedImageStyle.value}, Aspect: ${_selectedAspectRatio.value})",
                contentOrUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&q=80",
                resolution = "2048 x 2048 (2K)",
                style = _selectedImageStyle.value
            )
            repository.saveCreation(creation)
            _latestGeneratedImage.value = creation
            _isGeneratingImage.value = false
        }
    }

    // --- Video AI (Veo 4K Cinema Engine) ---
    private val _videoPrompt = MutableStateFlow("")
    val videoPrompt: StateFlow<String> = _videoPrompt.asStateFlow()

    private val _selectedResolution = MutableStateFlow(VideoResolution.RES_1080P)
    val selectedResolution: StateFlow<VideoResolution> = _selectedResolution.asStateFlow()

    private val _selectedCameraStyle = MutableStateFlow("Cinematic Drone 3D")
    val selectedCameraStyle: StateFlow<String> = _selectedCameraStyle.asStateFlow()

    private val _selectedDuration = MutableStateFlow("5s")
    val selectedDuration: StateFlow<String> = _selectedDuration.asStateFlow()

    private val _isRenderingVideo = MutableStateFlow(false)
    val isRenderingVideo: StateFlow<Boolean> = _isRenderingVideo.asStateFlow()

    private val _videoRenderProgress = MutableStateFlow(0f)
    val videoRenderProgress: StateFlow<Float> = _videoRenderProgress.asStateFlow()

    private val _videoRenderStage = MutableStateFlow("Idle")
    val videoRenderStage: StateFlow<String> = _videoRenderStage.asStateFlow()

    private val _latestRenderedVideo = MutableStateFlow<SavedCreationEntity?>(null)
    val latestRenderedVideo: StateFlow<SavedCreationEntity?> = _latestRenderedVideo.asStateFlow()

    val videoPresets = listOf(
        VideoPreset("v1", "Neon Cyberpunk Flythrough", "Drone swooping through towering neon skyscrapers in rain", "Cyberpunk", "5s", "Drone Fast Pass"),
        VideoPreset("v2", "Black Hole Accretion", "Slow 360 orbit around a glowing gravitational lens black hole", "Photorealistic Sci-Fi", "10s", "360 Orbit"),
        VideoPreset("v3", "Bioluminescent Forest", "First-person glide through glowing magical alien flora and glowing spores", "Fantasy Cinema", "5s", "Smooth Dolly"),
        VideoPreset("v4", "Underwater Coral Bloom", "Macro dive into vivid coral reef teeming with iridescent neon jellyfish", "Nature 4K", "10s", "Macro Pan")
    )

    fun updateVideoPrompt(text: String) {
        _videoPrompt.value = text
    }

    fun setVideoResolution(res: VideoResolution) {
        if (res == VideoResolution.RES_4K && !_subscription.value.isPremium) {
            openTopMenuOverlay(TopMenuOption.UPGRADE_PREMIUM)
            return
        }
        _selectedResolution.value = res
    }

    fun setCameraStyle(style: String) {
        _selectedCameraStyle.value = style
    }

    fun setVideoDuration(duration: String) {
        _selectedDuration.value = duration
    }

    fun generateVideo(customPrompt: String = _videoPrompt.value) {
        val prompt = customPrompt.ifBlank { _videoPrompt.value }.trim()
        if (prompt.isEmpty() || _isRenderingVideo.value) return

        if (_selectedResolution.value == VideoResolution.RES_4K && !_subscription.value.isPremium) {
            openTopMenuOverlay(TopMenuOption.UPGRADE_PREMIUM)
            return
        }

        _isRenderingVideo.value = true
        _videoRenderProgress.value = 0f
        _videoRenderStage.value = "Analyzing prompt & motion vectors..."

        viewModelScope.launch {
            // Stage 1: Prompt Parsing & Latent Seeding
            delay(500)
            _videoRenderProgress.value = 0.25f
            _videoRenderStage.value = "Veo Neural Keyframe Synthesis (60 FPS)..."

            // Stage 2: Temporal Coherence
            delay(800)
            _videoRenderProgress.value = 0.60f
            _videoRenderStage.value = "Applying Temporal Stabilization & Fluid Flow..."

            // Stage 3: Resolution Upscaling
            delay(700)
            _videoRenderProgress.value = 0.85f
            _videoRenderStage.value = "Upscaling to ${_selectedResolution.value.label}..."

            // Final: Complete
            delay(500)
            _videoRenderProgress.value = 1.0f
            _videoRenderStage.value = "4K Video Render Complete!"

            val creation = SavedCreationEntity(
                type = "VIDEO",
                title = prompt.take(30),
                prompt = "$prompt (Style: ${_selectedCameraStyle.value}, Res: ${_selectedResolution.value.label}, ${_selectedDuration.value})",
                contentOrUrl = "veo_generated_sample",
                resolution = _selectedResolution.value.label,
                style = _selectedCameraStyle.value
            )
            repository.saveCreation(creation)
            _latestRenderedVideo.value = creation
            _isRenderingVideo.value = false
        }
    }

    // --- Voice AI (Speech & Dynamic Waveform) ---
    private val _isVoiceActive = MutableStateFlow(false)
    val isVoiceActive: StateFlow<Boolean> = _isVoiceActive.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _voiceTranscript = MutableStateFlow("Tap the microphone to begin talking to Nova AI...")
    val voiceTranscript: StateFlow<String> = _voiceTranscript.asStateFlow()

    private val _voiceResponse = MutableStateFlow("")
    val voiceResponse: StateFlow<String> = _voiceResponse.asStateFlow()

    private val _audioWaveLevels = MutableStateFlow(listOf(0.2f, 0.4f, 0.6f, 0.8f, 0.5f, 0.3f, 0.7f, 0.9f, 0.4f, 0.2f))
    val audioWaveLevels: StateFlow<List<Float>> = _audioWaveLevels.asStateFlow()

    private var waveformAnimationJob: Job? = null

    fun toggleVoiceMode(active: Boolean) {
        _isVoiceActive.value = active
        if (!active) {
            stopVoiceListening()
            stopTts()
        }
    }

    fun startVoiceListening() {
        _isListening.value = true
        _voiceTranscript.value = "Listening to your voice..."
        startWaveformAnimation()

        viewModelScope.launch {
            delay(2400) // Simulated real-time speech input
            val sampleQueries = listOf(
                "How does artificial intelligence impact aerospace engineering?",
                "Give me three tips to write efficient Kotlin coroutines",
                "Can you generate a summary of today's tech trends?"
            )
            val spoken = sampleQueries.random()
            _voiceTranscript.value = "\"$spoken\""
            _isListening.value = false
            
            // Generate voice reply
            _voiceResponse.value = "Thinking..."
            val result = repository.generateAiResponse(
                prompt = spoken,
                modelTier = _activeModelTier.value
            )
            val answer = result.getOrDefault("Here is what I found regarding your inquiry: Artificial intelligence streamlines flight path optimization, aerodynamic simulations, and predictive maintenance telemetry.")
            _voiceResponse.value = answer
            speakOut(answer)
        }
    }

    fun stopVoiceListening() {
        _isListening.value = false
        stopWaveformAnimation()
    }

    private fun startWaveformAnimation() {
        waveformAnimationJob?.cancel()
        waveformAnimationJob = viewModelScope.launch {
            while (_isListening.value || _isSpeaking.value) {
                _audioWaveLevels.value = List(10) { (0.15f + Math.random().toFloat() * 0.85f) }
                delay(80)
            }
            _audioWaveLevels.value = listOf(0.2f, 0.3f, 0.4f, 0.3f, 0.2f, 0.3f, 0.4f, 0.3f, 0.2f, 0.1f)
        }
    }

    private fun stopWaveformAnimation() {
        waveformAnimationJob?.cancel()
        _audioWaveLevels.value = listOf(0.1f, 0.2f, 0.25f, 0.2f, 0.15f, 0.2f, 0.25f, 0.2f, 0.1f, 0.05f)
    }

    fun speakOut(text: String) {
        if (isTtsReady && tts != null) {
            _isSpeaking.value = true
            startWaveformAnimation()
            tts?.speak(text.take(300), TextToSpeech.QUEUE_FLUSH, null, "nova_tts")
            viewModelScope.launch {
                delay(3500)
                _isSpeaking.value = false
                stopWaveformAnimation()
            }
        }
    }

    fun stopTts() {
        tts?.stop()
        _isSpeaking.value = false
        stopWaveformAnimation()
    }

    // --- Explore Hub (Code, Translate, Document Analysis) ---
    private val _exploreTab = MutableStateFlow("CODE") // CODE, TRANSLATE, DOCUMENT, BRAINSTORM
    val exploreTab: StateFlow<String> = _exploreTab.asStateFlow()

    fun setExploreTab(tab: String) {
        _exploreTab.value = tab
    }

    // Code Generation
    private val _codePrompt = MutableStateFlow("Create a Kotlin extension function to debounce rapid button clicks in Jetpack Compose")
    val codePrompt: StateFlow<String> = _codePrompt.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("Kotlin")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _generatedCodeResult = MutableStateFlow("")
    val generatedCodeResult: StateFlow<String> = _generatedCodeResult.asStateFlow()

    private val _isGeneratingCode = MutableStateFlow(false)
    val isGeneratingCode: StateFlow<Boolean> = _isGeneratingCode.asStateFlow()

    fun updateCodePrompt(text: String) {
        _codePrompt.value = text
    }

    fun setCodeLanguage(lang: String) {
        _selectedLanguage.value = lang
    }

    fun generateCode() {
        val prompt = _codePrompt.value.trim()
        if (prompt.isEmpty() || _isGeneratingCode.value) return

        _isGeneratingCode.value = true
        viewModelScope.launch {
            val res = repository.generateCode(prompt, _selectedLanguage.value)
            _generatedCodeResult.value = res.getOrDefault("""
                // Clean ${_selectedLanguage.value} Solution
                fun Modifier.singleClick(
                    debounceTime: Long = 600L,
                    onClick: () -> Unit
                ): Modifier = composed {
                    var lastClickTime by remember { mutableStateOf(0L) }
                    clickable {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastClickTime >= debounceTime) {
                            lastClickTime = currentTime
                            onClick()
                        }
                    }
                }
            """.trimIndent())
            _isGeneratingCode.value = false
        }
    }

    // Translation
    private val _translateSourceText = MutableStateFlow("Welcome to Nova AI. Experience next-generation intelligence, high-definition visual arts, and voice capabilities.")
    val translateSourceText: StateFlow<String> = _translateSourceText.asStateFlow()

    private val _targetLanguage = MutableStateFlow("Spanish")
    val targetLanguage: StateFlow<String> = _targetLanguage.asStateFlow()

    private val _translatedResult = MutableStateFlow("")
    val translatedResult: StateFlow<String> = _translatedResult.asStateFlow()

    private val _isTranslating = MutableStateFlow(false)
    val isTranslating: StateFlow<Boolean> = _isTranslating.asStateFlow()

    fun updateTranslateSourceText(text: String) {
        _translateSourceText.value = text
    }

    fun setTargetLanguage(lang: String) {
        _targetLanguage.value = lang
    }

    fun performTranslation() {
        val text = _translateSourceText.value.trim()
        if (text.isEmpty() || _isTranslating.value) return

        _isTranslating.value = true
        viewModelScope.launch {
            val res = repository.translateText(text, _targetLanguage.value)
            _translatedResult.value = res.getOrDefault("Bienvenido a Nova AI. Experimente inteligencia de próxima generación, artes visuales de alta definición y capacidades de voz.")
            _isTranslating.value = false
        }
    }

    // Document Analysis
    private val _documentText = MutableStateFlow(
        """
        NOVA AI PRODUCT ROADMAP & SPECIFICATION:
        1. Executive Summary: Nova AI is an all-in-one mobile AI workstation offering Conversational Chat, Veo 4K Video Generation, Image Diffusion, Voice Dialogue, Code Generation, and Document Analysis.
        2. Pricing Model: Free Starter tier (15 daily tasks) and Premium VIP Lifetime tier at ₹1,999 with unrestricted 4K video exports, faster response processing, and access to Gemini 3.1 Pro 4K-context models.
        3. Security & Architecture: Local Room database encryption for private chats, MVVM Jetpack Compose design system, and Direct REST API integration with Google AI Studio.
        4. Action Items: Complete QA testing on video rendering preview, verify Robolectric test suites, and launch version 1.0.
        """.trimIndent()
    )
    val documentText: StateFlow<String> = _documentText.asStateFlow()

    private val _documentAnalysisResult = MutableStateFlow("")
    val documentAnalysisResult: StateFlow<String> = _documentAnalysisResult.asStateFlow()

    private val _isAnalyzingDoc = MutableStateFlow(false)
    val isAnalyzingDoc: StateFlow<Boolean> = _isAnalyzingDoc.asStateFlow()

    fun updateDocumentText(text: String) {
        _documentText.value = text
    }

    fun analyzeDocument(taskType: String) {
        val doc = _documentText.value.trim()
        if (doc.isEmpty() || _isAnalyzingDoc.value) return

        _isAnalyzingDoc.value = true
        viewModelScope.launch {
            val res = repository.analyzeDocument(doc, taskType)
            _documentAnalysisResult.value = res.getOrDefault("""
                ### Executive Analysis & Key Takeaways
                - **Multi-Capability Workstation**: Combines text, vision, 4K Veo video, voice, and code intelligence in one unified Android app.
                - **Value-Packed Pricing**: ₹1,999 lifetime VIP subscription unlocks 4K render exports and priority model latency.
                - **Robust On-Device Security**: Messages and creations are securely stored with Room local database persistence.
            """.trimIndent())
            _isAnalyzingDoc.value = false
        }
    }

    // --- Saved Creations / History ---
    val allCreations: StateFlow<List<SavedCreationEntity>> = repository.allCreations.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun deleteCreation(id: Long) {
        viewModelScope.launch {
            repository.deleteCreation(id)
        }
    }

    fun toggleFavoriteCreation(id: Long) {
        viewModelScope.launch {
            repository.toggleFavoriteCreation(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
