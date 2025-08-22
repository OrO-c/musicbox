package com.example.universalvoicebox.data.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * 下载服务接口
 */
interface DownloadService {
    /**
     * 下载文件
     */
    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody
}