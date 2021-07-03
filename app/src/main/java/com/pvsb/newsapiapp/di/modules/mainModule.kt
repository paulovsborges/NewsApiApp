package com.pvsb.newsapiapp.di.modules

import com.pvsb.newsapiapp.data.api.NewsApi
import com.pvsb.newsapiapp.data.repository.Repository
import com.pvsb.newsapiapp.data.repository.RepositoryImpl
import com.pvsb.newsapiapp.data.retrofit.createApi
import com.pvsb.newsapiapp.data.retrofit.provideOkHttpClient
import com.pvsb.newsapiapp.data.retrofit.provideRetrofit
import com.pvsb.newsapiapp.presentation.mainPresentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel { MainViewModel(repository = get()) }
}

val repositoryModule = module {
    single <Repository>{ RepositoryImpl(api = get()) }
}

val networkModule = module{
    single { provideOkHttpClient() }
    single { provideRetrofit(okHttpClient = get()) }
    single {  createApi<NewsApi>(retrofit = (get()))  }
}