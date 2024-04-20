package com.love.geminiandroidworkshoplagos.ui.feature.photo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PhotoReasoningViewModel() : ViewModel() {

    private val _uiState: MutableStateFlow<PhotoReasoningUiState> =
        MutableStateFlow(PhotoReasoningUiState.Initial)
    val uiState: StateFlow<PhotoReasoningUiState> =
        _uiState.asStateFlow()

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
