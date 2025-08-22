package com.example.universalvoicebox.domain.usecase

import com.example.universalvoicebox.domain.model.ImportResult
import com.example.universalvoicebox.domain.model.ImportType
import com.example.universalvoicebox.domain.repository.VoicePackRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 导入语音包用例
 */
class ImportVoicePackUseCase @Inject constructor(
    private val voicePackRepository: VoicePackRepository
) {
    /**
     * 从网络导入
     */
    suspend operator fun invoke(url: String): Flow<ImportResult> {
        return voicePackRepository.importFromNetwork(url)
    }
    
    /**
     * 从本地导入
     */
    suspend operator fun invoke(uri: String, type: ImportType): Flow<ImportResult> {
        return when (type) {
            ImportType.LOCAL -> voicePackRepository.importFromLocal(uri)
            ImportType.NETWORK -> voicePackRepository.importFromNetwork(uri)
        }
    }
}