package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.RenderMessageBody
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
fun ExploreScreen(
    currentTab: String,
    // Code
    codePrompt: String,
    selectedLanguage: String,
    codeResult: String,
    isGeneratingCode: Boolean,
    onCodePromptChanged: (String) -> Unit,
    onCodeLanguageSelected: (String) -> Unit,
    onGenerateCode: () -> Unit,
    // Translation
    sourceText: String,
    targetLanguage: String,
    translatedResult: String,
    isTranslating: Boolean,
    onSourceTextChanged: (String) -> Unit,
    onTargetLanguageSelected: (String) -> Unit,
    onTranslate: () -> Unit,
    // Document Analysis
    documentText: String,
    documentResult: String,
    isAnalyzingDoc: Boolean,
    onDocumentTextChanged: (String) -> Unit,
    onAnalyzeDoc: (String) -> Unit,
    onTabSelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val tabs = listOf(
        ExploreTabItem("CODE", "Code AI", Icons.Default.Code),
        ExploreTabItem("TRANSLATE", "Translator", Icons.Default.Translate),
        ExploreTabItem("DOCUMENT", "Doc Insights", Icons.Default.Description)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NaturalBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = null,
                tint = NaturalPrimary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "AI Capabilities & Tools Hub",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalTextPrimary
            )
        }

        // Horizontal Category Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEach { tab ->
                val isSelected = currentTab == tab.id
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) NaturalPrimary else NaturalSurface,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(tab.id) }
                        .border(
                            width = 1.dp,
                            color = if (isSelected) NaturalPrimary else NaturalCardBorder.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .testTag("explore_tab_${tab.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else NaturalTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tab.title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else NaturalTextSecondary
                        )
                    }
                }
            }
        }

        when (currentTab) {
            "CODE" -> {
                CodeGenerationView(
                    prompt = codePrompt,
                    language = selectedLanguage,
                    result = codeResult,
                    isGenerating = isGeneratingCode,
                    onPromptChanged = onCodePromptChanged,
                    onLanguageSelected = onCodeLanguageSelected,
                    onGenerate = onGenerateCode
                )
            }
            "TRANSLATE" -> {
                TranslationView(
                    sourceText = sourceText,
                    targetLanguage = targetLanguage,
                    result = translatedResult,
                    isTranslating = isTranslating,
                    onSourceTextChanged = onSourceTextChanged,
                    onTargetLanguageSelected = onTargetLanguageSelected,
                    onTranslate = onTranslate
                )
            }
            "DOCUMENT" -> {
                DocumentAnalysisView(
                    docText = documentText,
                    result = documentResult,
                    isAnalyzing = isAnalyzingDoc,
                    onDocTextChanged = onDocumentTextChanged,
                    onAnalyze = onAnalyzeDoc
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

data class ExploreTabItem(val id: String, val title: String, val icon: ImageVector)

@Composable
fun CodeGenerationView(
    prompt: String,
    language: String,
    result: String,
    isGenerating: Boolean,
    onPromptChanged: (String) -> Unit,
    onLanguageSelected: (String) -> Unit,
    onGenerate: () -> Unit
) {
    val languages = listOf("Kotlin", "Python", "TypeScript", "Swift", "Rust", "SQL")

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        OutlinedTextField(
            value = prompt,
            onValueChange = onPromptChanged,
            placeholder = { Text("Describe the algorithm, component, or system you want to build...", color = NaturalTextTertiary) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .testTag("code_prompt_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = NaturalTextPrimary,
                unfocusedTextColor = NaturalTextPrimary,
                focusedBorderColor = NaturalPrimary,
                unfocusedBorderColor = NaturalCardBorder,
                focusedContainerColor = NaturalSurface,
                unfocusedContainerColor = NaturalSurface
            )
        )

        // Language chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(languages) { lang ->
                val isSelected = language == lang
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) NaturalSagePill else NaturalSurface,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onLanguageSelected(lang) }
                        .border(
                            1.dp,
                            if (isSelected) NaturalPrimary else NaturalCardBorder.copy(alpha = 0.6f),
                            RoundedCornerShape(10.dp)
                        )
                ) {
                    Text(
                        text = lang,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) NaturalPrimary else NaturalTextSecondary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Button(
            onClick = onGenerate,
            enabled = !isGenerating && prompt.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("generate_code_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NaturalPrimary,
                contentColor = Color.White
            )
        ) {
            if (isGenerating) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
            } else {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = Icons.Default.Code, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("Generate Clean Code", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (result.isNotBlank()) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Generated $language Output",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    RenderMessageBody(content = result, isUser = false)
                }
            }
        }
    }
}

@Composable
fun TranslationView(
    sourceText: String,
    targetLanguage: String,
    result: String,
    isTranslating: Boolean,
    onSourceTextChanged: (String) -> Unit,
    onTargetLanguageSelected: (String) -> Unit,
    onTranslate: () -> Unit
) {
    val languages = listOf("Spanish", "French", "German", "Japanese", "Hindi", "Mandarin", "Arabic", "Portuguese")

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        OutlinedTextField(
            value = sourceText,
            onValueChange = onSourceTextChanged,
            placeholder = { Text("Enter text to translate...", color = NaturalTextTertiary) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .testTag("translate_input_text"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = NaturalTextPrimary,
                unfocusedTextColor = NaturalTextPrimary,
                focusedBorderColor = NaturalPrimary,
                unfocusedBorderColor = NaturalCardBorder,
                focusedContainerColor = NaturalSurface,
                unfocusedContainerColor = NaturalSurface
            )
        )

        Text("Target Language", fontSize = 12.sp, color = NaturalTextSecondary)

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(languages) { lang ->
                val isSelected = targetLanguage == lang
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) NaturalSagePill else NaturalSurface,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onTargetLanguageSelected(lang) }
                        .border(
                            1.dp,
                            if (isSelected) NaturalPrimary else NaturalCardBorder.copy(alpha = 0.6f),
                            RoundedCornerShape(10.dp)
                        )
                ) {
                    Text(
                        text = lang,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) NaturalPrimary else NaturalTextSecondary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Button(
            onClick = onTranslate,
            enabled = !isTranslating && sourceText.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("translate_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NaturalPrimary,
                contentColor = Color.White
            )
        ) {
            if (isTranslating) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
            } else {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = Icons.Default.Translate, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("Translate to $targetLanguage", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (result.isNotBlank()) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "$targetLanguage Translation",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = result,
                        fontSize = 14.sp,
                        color = NaturalTextPrimary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DocumentAnalysisView(
    docText: String,
    result: String,
    isAnalyzing: Boolean,
    onDocTextChanged: (String) -> Unit,
    onAnalyze: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        OutlinedTextField(
            value = docText,
            onValueChange = onDocTextChanged,
            placeholder = { Text("Paste document text, research papers, or meeting minutes...", color = NaturalTextTertiary) },
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .testTag("doc_input_text"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = NaturalTextPrimary,
                unfocusedTextColor = NaturalTextPrimary,
                focusedBorderColor = NaturalPrimary,
                unfocusedBorderColor = NaturalCardBorder,
                focusedContainerColor = NaturalSurface,
                unfocusedContainerColor = NaturalSurface
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onAnalyze("Summary") },
                enabled = !isAnalyzing && docText.isNotBlank(),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NaturalSagePill,
                    contentColor = NaturalPrimary
                )
            ) {
                Text("Summary", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { onAnalyze("Key Takeaways") },
                enabled = !isAnalyzing && docText.isNotBlank(),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NaturalSagePill,
                    contentColor = NaturalPrimary
                )
            ) {
                Text("Key Insights", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { onAnalyze("Action Items") },
                enabled = !isAnalyzing && docText.isNotBlank(),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NaturalSagePill,
                    contentColor = NaturalPrimary
                )
            ) {
                Text("Actions", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (isAnalyzing) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = NaturalPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Extracting document semantic intelligence...", color = NaturalPrimary, fontSize = 12.sp)
            }
        }

        if (result.isNotBlank()) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NaturalCardBorder.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Document Analysis Findings",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    RenderMessageBody(content = result, isUser = false)
                }
            }
        }
    }
}
