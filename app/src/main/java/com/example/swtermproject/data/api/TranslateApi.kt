package com.example.swtermproject.data.api

import com.example.swtermproject.data.model.TranslateApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslateApi {
    @GET("language/translate/v2")
    suspend fun translate(
        @Query("q") text: String,
        @Query("target") targetLanguage: String,
        @Query("key") apiKey: String
    ): TranslateApiResponse
}
