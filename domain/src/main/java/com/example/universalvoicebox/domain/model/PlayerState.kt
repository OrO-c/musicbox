package com.example.universalvoicebox.domain.model

/**
 * 播放器状态密封类
 */
sealed class PlayerState {
    object Idle : PlayerState()
    object Loading : PlayerState()
    data class Playing(
        val voice: Voice,
        val currentPosition: Long,
        val duration: Long,
        val isShortAudio: Boolean = false
    ) : PlayerState()
    data class Paused(
        val voice: Voice,
        val currentPosition: Long,
        val duration: Long
    ) : PlayerState()
    data class Error(val message: String) : PlayerState()
}

/**
 * 播放器操作
 */
sealed class PlayerAction {
    data class Play(val voice: Voice) : PlayerAction()
    object Pause : PlayerAction()
    object Resume : PlayerAction()
    object Stop : PlayerAction()
    data class SeekTo(val position: Long) : PlayerAction()
}