package com.pvsb.newsapiapp.data.mapper

import com.pvsb.newsapiapp.data.response.NewsBodyResponse
import com.pvsb.newsapiapp.data.response.NewsDetailsResponse

object NewsResultToMapper {

    fun resultMap(source: NewsBodyResponse): List<NewsDetailsResponse>{
        val newsResultList: MutableList<NewsDetailsResponse> = mutableListOf()
        source.let {newsBodyResponse ->
            for (newsResult in newsBodyResponse.newsDetails){
                newsResultList.add(newsResult)
            }
        }
        return newsResultList
    }
}