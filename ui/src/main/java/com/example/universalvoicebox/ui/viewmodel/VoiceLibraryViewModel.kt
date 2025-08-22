package com.example.universalvoicebox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universalvoicebox.domain.model.PlayerState
import com.example.universalvoicebox.domain.model.Voice
import com.example.universalvoicebox.domain.model.VoicePack
import com.example.universalvoicebox.domain.repository.AudioPlayerRepository
import com.example.universalvoicebox.domain.repository.VoicePackRepository
import com.example.universalvoicebox.domain.usecase.LoadVoicePackUseCase
import com.example.universalvoicebox.domain.usecase.PlayAudioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 语音库界面ViewModel
 */
@HiltViewModel
class VoiceLibraryViewModel @Inject constructor(
    private val voicePackRepository: VoicePackRepository,
    private val audioPlayerRepository: AudioPlayerRepository,
    private val loadVoicePackUseCase: LoadVoicePackUseCase,
    private val playAudioUseCase: PlayAudioUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(VoiceLibraryUiState())
    val uiState: StateFlow<VoiceLibraryUiState> = _uiState.asStateFlow()
    
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Idle)
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()
    
    init {
        loadVoicePack()
        observePlayerState()
    }
    
    /**
     * 加载语音包
     */
    private fun loadVoicePack() {
        viewModelScope.launch {
            loadVoicePackUseCase()
        }
        
        viewModelScope.launch {
            voicePackRepository.getCurrentVoicePack().collect { voicePack ->
                _uiState.value = _uiState.value.copy(
                    voicePack = voicePack,
                    isLoading = false
                )
            }
        }
    }
    
    /**
     * 观察播放器状态
     */
    private fun observePlayerState() {
        viewModelScope.launch {
            audioPlayerRepository.getPlayerState().collect { state ->
                _playerState.value = state
            }
        }
    }
    
    /**
     * 播放语音
     */
    fun playVoice(voice: Voice) {
        viewModelScope.launch {
            playAudioUseCase(voice)
        }
    }
    
    /**
     * 暂停播放
     */
    fun pausePlayback() {
        viewModelScope.launch {
            playAudioUseCase()
        }
    }
    
    /**
     * 恢复播放
     */
    fun resumePlayback() {
        viewModelScope.launch {
            playAudioUseCase.resume()
        }
    }
    
    /**
     * 跳转到指定位置
     */
    fun seekTo(position: Long) {
        viewModelScope.launch {
            playAudioUseCase.seekTo(position)
        }
    }
    
    /**
     * 选择栏目
     */
    fun selectSection(sectionId: String) {
        _uiState.value = _uiState.value.copy(
            selectedSectionId = sectionId
        )
    }
    
    /**
     * 获取当前栏目的语音列表
     */
    fun getCurrentSectionVoices(): List<Voice> {
        val voicePack = _uiState.value.voicePack ?: return emptyList()
        val selectedSectionId = _uiState.value.selectedSectionId
        
        return if (selectedSectionId != null) {
            voicePack.voices.filter { it.sectionId == selectedSectionId }
        } else {
            voicePack.voices
        }
    }
}

/**
 * 语音库界面状态
 */
data class VoiceLibraryUiState(
    val voicePack: VoicePack? = null,
    val selectedSectionId: String? = null,
    val isLoading: Boolean = true
)