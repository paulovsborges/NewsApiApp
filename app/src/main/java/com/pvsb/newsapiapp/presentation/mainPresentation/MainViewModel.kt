package com.pvsb.newsapiapp.presentation.mainPresentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pvsb.newsapiapp.data.repository.Repository
import com.pvsb.newsapiapp.model.NewsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(

    private val repository: Repository
) : ViewModel() {

    val newsLiveData: MutableLiveData<List<NewsEntity>> = MutableLiveData()


    fun getNewsViewModel() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.getNews()
            }.onSuccess {newsResults ->
                val news: MutableList<NewsEntity> = mutableListOf()
                newsResults.let { listNewsResults ->
                    for(result in listNewsResults){
                        val new = result.getNewsModel()
                        news.add(new)
                    }
                }
                newsLiveData.postValue(news)
            }
        }
    }
}