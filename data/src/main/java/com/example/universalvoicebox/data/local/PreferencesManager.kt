package com.example.universalvoicebox.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 偏好设置管理器
 */
@Singleton
class PreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    
    companion object {
        private val CURRENT_VOICE_PACK_PATH = stringPreferencesKey("current_voice_pack_path")
    }
    
    /**
     * 获取当前语音包路径
     */
    val currentVoicePackPath: Flow<String?> = dataStore.data.map { preferences ->
        preferences[CURRENT_VOICE_PACK_PATH]
    }
    
    /**
     * 设置当前语音包路径
     */
    suspend fun setCurrentVoicePackPath(path: String) {
        dataStore.edit { preferences ->
            preferences[CURRENT_VOICE_PACK_PATH] = path
        }
    }
    
    /**
     * 清除当前语音包路径
     */
    suspend fun clearCurrentVoicePackPath() {
        dataStore.edit { preferences ->
            preferences.remove(CURRENT_VOICE_PACK_PATH)
        }
    }
}