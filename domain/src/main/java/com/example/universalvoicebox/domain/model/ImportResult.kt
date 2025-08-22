package com.example.universalvoicebox.domain.model

/**
 * 导入结果密封类
 */
sealed class ImportResult {
    object Success : ImportResult()
    data class Error(val message: String) : ImportResult()
    object Loading : ImportResult()
    data class Progress(val percentage: Int) : ImportResult()
}

/**
 * 导入方式枚举
 */
enum class ImportType {
    NETWORK,
    LOCAL
}