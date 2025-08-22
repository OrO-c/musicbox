package com.example.universalvoicebox.domain.usecase

import com.example.universalvoicebox.domain.model.PlayerAction
import com.example.universalvoicebox.domain.model.Voice
import com.example.universalvoicebox.domain.repository.AudioPlayerRepository
import javax.inject.Inject

/**
 * 播放音频用例
 */
class PlayAudioUseCase @Inject constructor(
    private val audioPlayerRepository: AudioPlayerRepository
) {
    /**
     * 播放语音
     */
    suspend operator fun invoke(voice: Voice) {
        audioPlayerRepository.executeAction(PlayerAction.Play(voice))
    }
    
    /**
     * 暂停播放
     */
    suspend operator fun invoke() {
        audioPlayerRepository.executeAction(PlayerAction.Pause)
    }
    
    /**
     * 恢复播放
     */
    suspend fun resume() {
        audioPlayerRepository.executeAction(PlayerAction.Resume)
    }
    
    /**
     * 停止播放
     */
    suspend fun stop() {
        audioPlayerRepository.executeAction(PlayerAction.Stop)
    }
    
    /**
     * 跳转到指定位置
     */
    suspend fun seekTo(position: Long) {
        audioPlayerRepository.executeAction(PlayerAction.SeekTo(position))
    }
}