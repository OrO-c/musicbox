package com.example.universalvoicebox.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.universalvoicebox.R
import com.example.universalvoicebox.domain.model.Voice
import com.example.universalvoicebox.ui.component.VoiceItem
import com.example.universalvoicebox.ui.component.MiniPlayer
import com.example.universalvoicebox.ui.component.NavigationDrawer
import com.example.universalvoicebox.ui.component.BottomNavigation
import com.example.universalvoicebox.ui.viewmodel.VoiceLibraryViewModel

/**
 * 语音库主界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceLibraryScreen(
    onNavigateToImport: () -> Unit,
    viewModel: VoiceLibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val playerState by viewModel.playerState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    var showDrawer by remember { mutableStateOf(false) }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        // TODO: 实现本地导入功能
        uri?.let { /* 处理本地文件导入 */ }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.voicePack?.title ?: stringResource(R.string.app_name)) },
                navigationIcon = {
                    if (uiState.voicePack?.sections?.size ?: 0 > 5) {
                        IconButton(onClick = { showDrawer = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.import_from_network)) },
                            onClick = {
                                showMenu = false
                                onNavigateToImport()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.import_from_local)) },
                            onClick = {
                                showMenu = false
                                launcher.launch("application/zip")
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (uiState.voicePack?.sections?.size ?: 0 <= 5) {
                BottomNavigation(
                    sections = uiState.voicePack?.sections ?: emptyList(),
                    selectedSectionId = uiState.selectedSectionId,
                    onSectionSelected = { viewModel.selectSection(it) }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                val voices = viewModel.getCurrentSectionVoices()
                if (voices.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_voices_found),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(voices) { voice ->
                            VoiceItem(
                                voice = voice,
                                isPlaying = playerState is com.example.universalvoicebox.domain.model.PlayerState.Playing && 
                                           (playerState as com.example.universalvoicebox.domain.model.PlayerState.Playing).voice.id == voice.id,
                                onClick = { viewModel.playVoice(voice) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
            
            // 迷你播放器
            if (playerState is com.example.universalvoicebox.domain.model.PlayerState.Playing && 
                !(playerState as com.example.universalvoicebox.domain.model.PlayerState.Playing).isShortAudio) {
                MiniPlayer(
                    playerState = playerState as com.example.universalvoicebox.domain.model.PlayerState.Playing,
                    onPause = { viewModel.pausePlayback() },
                    onResume = { viewModel.resumePlayback() },
                    onSeek = { viewModel.seekTo(it) }
                )
            }
        }
    }
    
    // 导航抽屉
    if (showDrawer) {
        NavigationDrawer(
            sections = uiState.voicePack?.sections ?: emptyList(),
            selectedSectionId = uiState.selectedSectionId,
            onSectionSelected = { 
                viewModel.selectSection(it)
                showDrawer = false
            },
            onDismiss = { showDrawer = false }
        )
    }
}