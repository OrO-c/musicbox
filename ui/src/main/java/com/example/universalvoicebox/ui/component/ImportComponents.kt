package com.example.universalvoicebox.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.universalvoicebox.R
import com.example.universalvoicebox.domain.model.ImportType

/**
 * 导入方式选择组件
 */
@Composable
fun ImportMethodSelection(
    onNetworkImportSelected: () -> Unit,
    onLocalImportSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "选择导入方式",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Button(
            onClick = onNetworkImportSelected,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CloudDownload,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(stringResource(R.string.import_from_network))
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onLocalImportSelected,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(stringResource(R.string.import_from_local))
        }
    }
}

/**
 * 网络导入输入组件
 */
@Composable
fun NetworkImportInput(
    url: String,
    onUrlChange: (String) -> Unit,
    onStartDownload: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.enter_url),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        OutlinedTextField(
            value = url,
            onValueChange = onUrlChange,
            label = { Text("URL") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onStartDownload,
            enabled = url.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(stringResource(R.string.start_download))
        }
    }
}

/**
 * 处理中组件
 */
@Composable
fun ProcessingView(
    progress: Int,
    importType: ImportType?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            progress = progress / 100f,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = when (importType) {
                ImportType.NETWORK -> stringResource(R.string.downloading, progress)
                ImportType.LOCAL -> stringResource(R.string.extracting)
                null -> stringResource(R.string.processing)
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * 结果组件
 */
@Composable
fun ResultView(
    isSuccess: Boolean,
    errorMessage: String,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isSuccess) stringResource(R.string.import_success) else stringResource(R.string.import_failed),
            style = MaterialTheme.typography.headlineMedium,
            color = if (isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )
        
        if (!isSuccess && errorMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!isSuccess) {
                Button(onClick = onRetry) {
                    Text(stringResource(R.string.retry))
                }
            }
            
            OutlinedButton(onClick = onBack) {
                Text(stringResource(R.string.back))
            }
        }
    }
}