package com.love.geminiandroidworkshoplagos.ui.feature.photo

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.love.geminiandroidworkshoplagos.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhotoReasoningViewModel() : ViewModel() {

    private val _uiState: MutableStateFlow<PhotoReasoningUiState> =
        MutableStateFlow(PhotoReasoningUiState.Initial)
    val uiState: StateFlow<PhotoReasoningUiState> =
        _uiState.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.0-pro-vision-latest",
        apiKey = BuildConfig.apiKey
    )

    fun reason(
        userInput: String,
        selectedImages: List<Bitmap>
    ) {
        _uiState.value = PhotoReasoningUiState.Loading
        val prompt = "Look at the image(s), and then answer the following question: $userInput"

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent = content {
                    for (bitmap in selectedImages) {
                        image(bitmap)
                    }
                    text(prompt)
                }

                val response = generativeModel.generateContent(inputContent)
                response.text?.let { outputContent ->
                    _uiState.value = PhotoReasoningUiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = PhotoReasoningUiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}

sealed interface PhotoReasoningUiState {

    data object Initial : PhotoReasoningUiState
    data object Loading : PhotoReasoningUiState
    data class Success(
        val outputText: String
    ) : PhotoReasoningUiState

    data class Error(
        val errorMessage: String
    ) : PhotoReasoningUiState
}
