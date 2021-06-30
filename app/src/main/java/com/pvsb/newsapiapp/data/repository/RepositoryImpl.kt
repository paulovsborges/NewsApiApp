package com.pvsb.newsapiapp.data.repository

import com.pvsb.newsapiapp.data.api.NewsApi
import com.pvsb.newsapiapp.data.mapper.NewsResultToMapper
import com.pvsb.newsapiapp.data.response.NewsDetailsResponse


class RepositoryImpl(private val api: NewsApi): Repository {
    override suspend fun getNews(): List<NewsDetailsResponse> {
        return NewsResultToMapper.resultMap(api.getNews())
    }
}