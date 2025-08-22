package com.example.universalvoicebox.data.di

import com.example.universalvoicebox.data.repository.AudioPlayerRepositoryImpl
import com.example.universalvoicebox.data.repository.VoicePackRepositoryImpl
import com.example.universalvoicebox.domain.repository.AudioPlayerRepository
import com.example.universalvoicebox.domain.repository.VoicePackRepository
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Data层依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    
    @Binds
    @Singleton
    abstract fun bindVoicePackRepository(
        voicePackRepositoryImpl: VoicePackRepositoryImpl
    ): VoicePackRepository
    
    @Binds
    @Singleton
    abstract fun bindAudioPlayerRepository(
        audioPlayerRepositoryImpl: AudioPlayerRepositoryImpl
    ): AudioPlayerRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataProvidesModule {
    
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}