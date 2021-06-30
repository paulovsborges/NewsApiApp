package com.pvsb.newsapiapp.data.response

import com.google.gson.annotations.SerializedName

data class NewsBodyResponse (

    @SerializedName("articles")
    val newsDetails: List<NewsDetailsResponse>
)