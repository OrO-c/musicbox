package com.example.universalvoicebox.domain.model

/**
 * 语音包配置数据模型
 * 对应index.json文件的结构
 */
data class VoicePack(
    val title: String,
    val sections: List<Section>,
    val voices: List<Voice>
)

/**
 * 栏目数据模型
 */
data class Section(
    val id: String,
    val name: String,
    val icon: String? = null
)

/**
 * 语音数据模型
 */
data class Voice(
    val id: String,
    val text: String,
    val audioFile: String,
    val sectionId: String,
    val duration: Long = 0L // 音频时长（毫秒）
)