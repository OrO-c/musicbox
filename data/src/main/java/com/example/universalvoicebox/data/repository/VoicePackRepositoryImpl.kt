package com.example.universalvoicebox.data.repository

import android.content.Context
import com.example.universalvoicebox.data.local.FileManager
import com.example.universalvoicebox.data.local.PreferencesManager
import com.example.universalvoicebox.data.network.DownloadService
import com.example.universalvoicebox.domain.model.ImportResult
import com.example.universalvoicebox.domain.model.Voice
import com.example.universalvoicebox.domain.model.VoicePack
import com.example.universalvoicebox.domain.repository.VoicePackRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 语音包仓库实现
 */
@Singleton
class VoicePackRepositoryImpl @Inject constructor(
    private val context: Context,
    private val fileManager: FileManager,
    private val preferencesManager: PreferencesManager,
    private val downloadService: DownloadService,
    private val gson: Gson
) : VoicePackRepository {
    
    private val _currentVoicePack = MutableStateFlow<VoicePack?>(null)
    
    override fun getCurrentVoicePack(): Flow<VoicePack?> = _currentVoicePack
    
    override suspend fun importFromNetwork(url: String): Flow<ImportResult> {
        val resultFlow = MutableStateFlow<ImportResult>(ImportResult.Loading)
        
        try {
            // 下载文件
            val fileName = "voice_pack_${System.currentTimeMillis()}.zip"
            val zipFile = fileManager.downloadFile(url, fileName) { progress ->
                resultFlow.value = ImportResult.Progress(progress)
            }
            
            // 解压文件
            resultFlow.value = ImportResult.Loading
            val extractDir = File(context.filesDir, "voice_packs/${System.currentTimeMillis()}")
            val success = fileManager.extractZipFile(zipFile, extractDir)
            
            if (success) {
                // 加载语音包
                val voicePack = loadVoicePackFromDirectory(extractDir)
                if (voicePack != null) {
                    _currentVoicePack.value = voicePack
                    preferencesManager.setCurrentVoicePackPath(extractDir.absolutePath)
                    resultFlow.value = ImportResult.Success
                } else {
                    resultFlow.value = ImportResult.Error("Failed to load voice pack")
                }
            } else {
                resultFlow.value = ImportResult.Error("Invalid ZIP file or missing index.json")
            }
        } catch (e: Exception) {
            resultFlow.value = ImportResult.Error(e.message ?: "Unknown error")
        }
        
        return resultFlow
    }
    
    override suspend fun importFromLocal(uri: String): Flow<ImportResult> {
        val resultFlow = MutableStateFlow<ImportResult>(ImportResult.Loading)
        
        try {
            // 复制文件
            val fileName = "voice_pack_${System.currentTimeMillis()}.zip"
            val zipFile = fileManager.copyFileFromUri(android.net.Uri.parse(uri), fileName)
            
            // 解压文件
            val extractDir = File(context.filesDir, "voice_packs/${System.currentTimeMillis()}")
            val success = fileManager.extractZipFile(zipFile, extractDir)
            
            if (success) {
                // 加载语音包
                val voicePack = loadVoicePackFromDirectory(extractDir)
                if (voicePack != null) {
                    _currentVoicePack.value = voicePack
                    preferencesManager.setCurrentVoicePackPath(extractDir.absolutePath)
                    resultFlow.value = ImportResult.Success
                } else {
                    resultFlow.value = ImportResult.Error("Failed to load voice pack")
                }
            } else {
                resultFlow.value = ImportResult.Error("Invalid ZIP file or missing index.json")
            }
        } catch (e: Exception) {
            resultFlow.value = ImportResult.Error(e.message ?: "Unknown error")
        }
        
        return resultFlow
    }
    
    override suspend fun loadBuiltInVoicePack() {
        try {
            // 从assets加载内置语音包
            val indexJson = context.assets.open("default_voice_pack/index.json").bufferedReader().use { it.readText() }
            val voicePack = gson.fromJson(indexJson, VoicePack::class.java)
            _currentVoicePack.value = voicePack
        } catch (e: Exception) {
            // 如果内置语音包加载失败，创建一个空的语音包
            _currentVoicePack.value = VoicePack(
                title = "默认语音盒",
                sections = emptyList(),
                voices = emptyList()
            )
        }
    }
    
    override fun getAudioFilePath(audioFile: String): String {
        return fileManager.getAudioFilePath(audioFile)
    }
    
    override fun audioFileExists(audioFile: String): Boolean {
        return fileManager.fileExists(getAudioFilePath(audioFile))
    }
    
    /**
     * 从目录加载语音包
     */
    private fun loadVoicePackFromDirectory(directory: File): VoicePack? {
        return try {
            val indexFile = File(directory, "index.json")
            if (!indexFile.exists()) return null
            
            val indexJson = indexFile.readText()
            gson.fromJson(indexJson, VoicePack::class.java)
        } catch (e: Exception) {
            null
        }
    }
}