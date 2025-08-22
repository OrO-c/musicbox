package com.example.universalvoicebox.domain.repository

import com.example.universalvoicebox.domain.model.ImportResult
import com.example.universalvoicebox.domain.model.VoicePack
import kotlinx.coroutines.flow.Flow

/**
 * 语音包仓库接口
 */
interface VoicePackRepository {
    /**
     * 获取当前语音包
     */
    fun getCurrentVoicePack(): Flow<VoicePack?>
    
    /**
     * 从网络导入语音包
     */
    suspend fun importFromNetwork(url: String): Flow<ImportResult>
    
    /**
     * 从本地导入语音包
     */
    suspend fun importFromLocal(uri: String): Flow<ImportResult>
    
    /**
     * 加载内置语音包
     */
    suspend fun loadBuiltInVoicePack()
    
    /**
     * 获取音频文件路径
     */
    fun getAudioFilePath(audioFile: String): String
    
    /**
     * 检查音频文件是否存在
     */
    fun audioFileExists(audioFile: String): Boolean
}