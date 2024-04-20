package com.love.geminiandroidworkshoplagos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.love.geminiandroidworkshoplagos.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TextViewModel: ViewModel() {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.0-pro",
        apiKey = BuildConfig.apiKey,
    )

    private val _uiState: MutableStateFlow<SummarizeTextUiState> =
        MutableStateFlow(SummarizeTextUiState.Initial)
    val uiState: StateFlow<SummarizeTextUiState> =
        _uiState.asStateFlow()


    fun summarize(inputText: String) {
        _uiState.value = SummarizeTextUiState.Loading

        val prompt = "Summarize the following text for me: $inputText"

        viewModelScope.launch {
            // Non-streaming
            try {
                val response = generativeModel.generateContent(prompt)
                response.text?.let { outputContent ->
                    _uiState.value = SummarizeTextUiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = SummarizeTextUiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}

sealed interface SummarizeTextUiState {

    data object Initial: SummarizeTextUiState

    data object Loading: SummarizeTextUiState

    class Success(
        val outputText: String
    ): SummarizeTextUiState

    data class Error(
        val errorMessage: String
    ): SummarizeTextUiState
}