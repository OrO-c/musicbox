package com.example.universalvoicebox.domain.usecase

import com.example.universalvoicebox.domain.repository.VoicePackRepository
import javax.inject.Inject

/**
 * 加载语音包用例
 */
class LoadVoicePackUseCase @Inject constructor(
    private val voicePackRepository: VoicePackRepository
) {
    /**
     * 执行加载
     */
    suspend operator fun invoke() {
        voicePackRepository.loadBuiltInVoicePack()
    }
}