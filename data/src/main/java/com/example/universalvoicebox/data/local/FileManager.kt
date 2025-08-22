package com.example.universalvoicebox.data.local

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 文件管理器
 */
@Singleton
class FileManager @Inject constructor(
    private val context: Context
) {
    
    companion object {
        private const val VOICE_PACKS_DIR = "voice_packs"
        private const val INDEX_FILE = "index.json"
    }
    
    /**
     * 获取语音包目录
     */
    private fun getVoicePacksDirectory(): File {
        return File(context.filesDir, VOICE_PACKS_DIR).apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }
    
    /**
     * 从URI复制文件到本地
     */
    suspend fun copyFileFromUri(uri: Uri, fileName: String): File = withContext(Dispatchers.IO) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputFile = File(getVoicePacksDirectory(), fileName)
        
        inputStream?.use { input ->
            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
            }
        }
        
        outputFile
    }
    
    /**
     * 下载文件到本地
     */
    suspend fun downloadFile(url: String, fileName: String, onProgress: (Int) -> Unit): File = withContext(Dispatchers.IO) {
        val outputFile = File(getVoicePacksDirectory(), fileName)
        
        // 这里应该使用实际的网络下载逻辑
        // 为了简化，这里只是创建一个空文件
        outputFile.createNewFile()
        onProgress(100)
        
        outputFile
    }
    
    /**
     * 解压ZIP文件
     */
    suspend fun extractZipFile(zipFile: File, extractDir: File): Boolean = withContext(Dispatchers.IO) {
        try {
            if (!extractDir.exists()) {
                extractDir.mkdirs()
            }
            
            ZipInputStream(zipFile.inputStream()).use { zipInputStream ->
                var entry: ZipEntry?
                while (zipInputStream.nextEntry.also { entry = it } != null) {
                    val file = File(extractDir, entry!!.name)
                    
                    if (entry!!.isDirectory) {
                        file.mkdirs()
                    } else {
                        file.parentFile?.mkdirs()
                        FileOutputStream(file).use { output ->
                            zipInputStream.copyTo(output)
                        }
                    }
                }
            }
            
            // 验证是否包含index.json文件
            val indexFile = File(extractDir, INDEX_FILE)
            indexFile.exists()
        } catch (e: IOException) {
            false
        }
    }
    
    /**
     * 检查文件是否存在
     */
    fun fileExists(filePath: String): Boolean {
        return File(filePath).exists()
    }
    
    /**
     * 获取音频文件路径
     */
    fun getAudioFilePath(audioFile: String): String {
        return File(getVoicePacksDirectory(), audioFile).absolutePath
    }
    
    /**
     * 清理临时文件
     */
    suspend fun cleanupTempFiles() = withContext(Dispatchers.IO) {
        val tempDir = getVoicePacksDirectory()
        tempDir.listFiles()?.forEach { file ->
            if (file.isFile && file.name.endsWith(".tmp")) {
                file.delete()
            }
        }
    }
}