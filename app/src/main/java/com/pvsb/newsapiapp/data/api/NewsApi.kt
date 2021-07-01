package com.pvsb.newsapiapp.data.api

import com.pvsb.newsapiapp.data.response.NewsBodyResponse
import com.pvsb.newsapiapp.model.NewsEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/everything?q=tesla&apiKey=eb14adb4f30842efa48f919d726b52e8")
    suspend fun getNews(
     //   @Query("articles")sources : String = ""

    ) : NewsBodyResponse
}