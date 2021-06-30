package com.pvsb.newsapiapp.data.repository

import com.pvsb.newsapiapp.data.response.NewsDetailsResponse

interface Repository {

    suspend fun getNews(): List<NewsDetailsResponse>

}