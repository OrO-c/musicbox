package com.example.universalvoicebox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universalvoicebox.domain.model.ImportResult
import com.example.universalvoicebox.domain.model.ImportType
import com.example.universalvoicebox.domain.usecase.ImportVoicePackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 导入向导界面ViewModel
 */
@HiltViewModel
class ImportWizardViewModel @Inject constructor(
    private val importVoicePackUseCase: ImportVoicePackUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ImportWizardUiState())
    val uiState: StateFlow<ImportWizardUiState> = _uiState.asStateFlow()
    
    /**
     * 选择导入方式
     */
    fun selectImportType(type: ImportType) {
        _uiState.value = _uiState.value.copy(
            selectedImportType = type,
            currentStep = ImportStep.SELECT_METHOD
        )
    }
    
    /**
     * 设置网络URL
     */
    fun setNetworkUrl(url: String) {
        _uiState.value = _uiState.value.copy(
            networkUrl = url
        )
    }
    
    /**
     * 开始网络导入
     */
    fun startNetworkImport() {
        val url = _uiState.value.networkUrl
        if (url.isBlank()) return
        
        _uiState.value = _uiState.value.copy(
            currentStep = ImportStep.PROCESSING
        )
        
        viewModelScope.launch {
            importVoicePackUseCase(url).collect { result ->
                when (result) {
                    is ImportResult.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            currentStep = ImportStep.PROCESSING
                        )
                    }
                    is ImportResult.Progress -> {
                        _uiState.value = _uiState.value.copy(
                            currentStep = ImportStep.PROCESSING,
                            progress = result.percentage
                        )
                    }
                    is ImportResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            currentStep = ImportStep.RESULT,
                            isSuccess = true
                        )
                    }
                    is ImportResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            currentStep = ImportStep.RESULT,
                            isSuccess = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }
    
    /**
     * 开始本地导入
     */
    fun startLocalImport(uri: String) {
        _uiState.value = _uiState.value.copy(
            currentStep = ImportStep.PROCESSING
        )
        
        viewModelScope.launch {
            importVoicePackUseCase(uri, ImportType.LOCAL).collect { result ->
                when (result) {
                    is ImportResult.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            currentStep = ImportStep.PROCESSING
                        )
                    }
                    is ImportResult.Progress -> {
                        _uiState.value = _uiState.value.copy(
                            currentStep = ImportStep.PROCESSING,
                            progress = result.percentage
                        )
                    }
                    is ImportResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            currentStep = ImportStep.RESULT,
                            isSuccess = true
                        )
                    }
                    is ImportResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            currentStep = ImportStep.RESULT,
                            isSuccess = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }
    
    /**
     * 重置状态
     */
    fun reset() {
        _uiState.value = ImportWizardUiState()
    }
    
    /**
     * 返回上一步
     */
    fun goBack() {
        val currentStep = _uiState.value.currentStep
        _uiState.value = when (currentStep) {
            ImportStep.SELECT_METHOD -> _uiState.value.copy(
                selectedImportType = null
            )
            ImportStep.NETWORK_INPUT -> _uiState.value.copy(
                currentStep = ImportStep.SELECT_METHOD
            )
            ImportStep.PROCESSING -> _uiState.value.copy(
                currentStep = ImportStep.SELECT_METHOD
            )
            ImportStep.RESULT -> _uiState.value.copy(
                currentStep = ImportStep.SELECT_METHOD
            )
        }
    }
}

/**
 * 导入步骤枚举
 */
enum class ImportStep {
    SELECT_METHOD,
    NETWORK_INPUT,
    PROCESSING,
    RESULT
}

/**
 * 导入向导界面状态
 */
data class ImportWizardUiState(
    val selectedImportType: ImportType? = null,
    val networkUrl: String = "",
    val currentStep: ImportStep = ImportStep.SELECT_METHOD,
    val progress: Int = 0,
    val isSuccess: Boolean = false,
    val errorMessage: String = ""
)