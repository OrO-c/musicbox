package com.example.universalvoicebox.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.universalvoicebox.R
import com.example.universalvoicebox.domain.model.PlayerState

/**
 * 迷你播放器组件
 */
@Composable
fun MiniPlayer(
    playerState: PlayerState.Playing,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSeek: (Long) -> Unit
) {
    var currentPosition by remember { mutableStateOf(playerState.currentPosition) }
    
    // 更新当前位置
    LaunchedEffect(playerState.currentPosition) {
        currentPosition = playerState.currentPosition
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 语音文本
            Text(
                text = playerState.voice.text,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 控制栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 播放/暂停按钮
                IconButton(
                    onClick = { onPause() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = stringResource(R.string.pause)
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // 进度条
                Slider(
                    value = currentPosition.toFloat(),
                    onValueChange = { onSeek(it.toLong()) },
                    valueRange = 0f..playerState.duration.toFloat(),
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // 时长显示
                Text(
                    text = "${formatDuration(currentPosition)} / ${formatDuration(playerState.duration)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 格式化时长
 */
private fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}