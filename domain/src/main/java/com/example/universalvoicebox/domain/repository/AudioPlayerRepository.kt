package com.example.universalvoicebox.domain.repository

import com.example.universalvoicebox.domain.model.PlayerAction
import com.example.universalvoicebox.domain.model.PlayerState
import kotlinx.coroutines.flow.Flow

/**
 * 音频播放器仓库接口
 */
interface AudioPlayerRepository {
    /**
     * 获取播放器状态
     */
    fun getPlayerState(): Flow<PlayerState>
    
    /**
     * 执行播放器操作
     */
    suspend fun executeAction(action: PlayerAction)
    
    /**
     * 获取音频时长
     */
    suspend fun getAudioDuration(audioPath: String): Long
    
    /**
     * 释放播放器资源
     */
    fun release()
}