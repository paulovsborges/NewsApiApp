package com.pvsb.newsapiapp.model

data class NewsEntity (

    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String

)