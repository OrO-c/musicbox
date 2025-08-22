package com.example.universalvoicebox.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.universalvoicebox.R
import com.example.universalvoicebox.domain.model.ImportType
import com.example.universalvoicebox.ui.component.ImportMethodSelection
import com.example.universalvoicebox.ui.component.NetworkImportInput
import com.example.universalvoicebox.ui.component.ProcessingView
import com.example.universalvoicebox.ui.component.ResultView
import com.example.universalvoicebox.ui.viewmodel.ImportWizardViewModel
import com.example.universalvoicebox.ui.viewmodel.ImportStep

/**
 * 导入向导界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportWizardScreen(
    onNavigateBack: () -> Unit,
    viewModel: ImportWizardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("导入语音包") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState.currentStep) {
                ImportStep.SELECT_METHOD -> {
                    ImportMethodSelection(
                        onNetworkImportSelected = { viewModel.selectImportType(ImportType.NETWORK) },
                        onLocalImportSelected = { viewModel.selectImportType(ImportType.LOCAL) }
                    )
                }
                ImportStep.NETWORK_INPUT -> {
                    NetworkImportInput(
                        url = uiState.networkUrl,
                        onUrlChange = { viewModel.setNetworkUrl(it) },
                        onStartDownload = { viewModel.startNetworkImport() }
                    )
                }
                ImportStep.PROCESSING -> {
                    ProcessingView(
                        progress = uiState.progress,
                        importType = uiState.selectedImportType
                    )
                }
                ImportStep.RESULT -> {
                    ResultView(
                        isSuccess = uiState.isSuccess,
                        errorMessage = uiState.errorMessage,
                        onRetry = { viewModel.reset() },
                        onBack = { viewModel.goBack() }
                    )
                }
            }
        }
    }
}