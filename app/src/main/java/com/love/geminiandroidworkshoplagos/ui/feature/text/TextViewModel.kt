package com.love.geminiandroidworkshoplagos.ui.feature.text

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TextViewModel: ViewModel() {

    private val _uiState: MutableStateFlow<SummarizeTextUiState> =
        MutableStateFlow(SummarizeTextUiState.Initial)
    val uiState: StateFlow<SummarizeTextUiState> =
        _uiState.asStateFlow()
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