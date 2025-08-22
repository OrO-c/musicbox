package com.example.universalvoicebox.data.repository

import android.content.Context
import android.media.MediaMetadataRetriever
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.universalvoicebox.domain.model.PlayerAction
import com.example.universalvoicebox.domain.model.PlayerState
import com.example.universalvoicebox.domain.model.Voice
import com.example.universalvoicebox.domain.repository.AudioPlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 音频播放器仓库实现
 */
@Singleton
class AudioPlayerRepositoryImpl @Inject constructor(
    private val context: Context
) : AudioPlayerRepository {
    
    private var exoPlayer: ExoPlayer? = null
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Idle)
    private var currentVoice: Voice? = null
    
    init {
        initializePlayer()
    }
    
    private fun initializePlayer() {
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_READY -> {
                            currentVoice?.let { voice ->
                                val isShortAudio = duration <= 10000 // 10秒
                                _playerState.value = PlayerState.Playing(
                                    voice = voice,
                                    currentPosition = currentPosition,
                                    duration = duration,
                                    isShortAudio = isShortAudio
                                )
                            }
                        }
                        Player.STATE_ENDED -> {
                            _playerState.value = PlayerState.Idle
                            currentVoice = null
                        }
                        Player.STATE_BUFFERING -> {
                            _playerState.value = PlayerState.Loading
                        }
                    }
                }
                
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (!isPlaying && _playerState.value is PlayerState.Playing) {
                        currentVoice?.let { voice ->
                            _playerState.value = PlayerState.Paused(
                                voice = voice,
                                currentPosition = currentPosition,
                                duration = duration
                            )
                        }
                    }
                }
            })
        }
    }
    
    override fun getPlayerState(): Flow<PlayerState> = _playerState
    
    override suspend fun executeAction(action: PlayerAction) {
        when (action) {
            is PlayerAction.Play -> {
                playVoice(action.voice)
            }
            is PlayerAction.Pause -> {
                exoPlayer?.pause()
            }
            is PlayerAction.Resume -> {
                exoPlayer?.play()
            }
            is PlayerAction.Stop -> {
                exoPlayer?.stop()
                _playerState.value = PlayerState.Idle
                currentVoice = null
            }
            is PlayerAction.SeekTo -> {
                exoPlayer?.seekTo(action.position)
            }
        }
    }
    
    override suspend fun getAudioDuration(audioPath: String): Long {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(audioPath)
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            retriever.release()
            duration?.toLong() ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
    
    override fun release() {
        exoPlayer?.release()
        exoPlayer = null
    }
    
    private fun playVoice(voice: Voice) {
        try {
            val audioFile = File(voice.audioFile)
            if (!audioFile.exists()) {
                _playerState.value = PlayerState.Error("Audio file not found")
                return
            }
            
            currentVoice = voice
            _playerState.value = PlayerState.Loading
            
            val mediaItem = MediaItem.fromUri(audioFile.absolutePath)
            exoPlayer?.apply {
                setMediaItem(mediaItem)
                prepare()
                play()
            }
        } catch (e: Exception) {
            _playerState.value = PlayerState.Error(e.message ?: "Unknown error")
        }
    }
}