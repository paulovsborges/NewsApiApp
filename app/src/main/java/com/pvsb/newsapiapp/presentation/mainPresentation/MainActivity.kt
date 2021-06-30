package com.pvsb.newsapiapp.presentation.mainPresentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pvsb.newsapiapp.databinding.ActivityMainBinding
import com.pvsb.newsapiapp.presentation.mainPresentation.adapter.MainAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var mAdapter: MainAdapter

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initiateAdapter()
        observeViewModel()
    }

    private fun observeViewModel() {

        viewModel.newsLiveData.observe(this, Observer { listNews ->
            mAdapter.getNews(listNews)
        })
        viewModel.getNewsViewModel()
    }

    private fun initiateAdapter() {
        with(binding.recyclerViewMain){
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            mAdapter = MainAdapter(this@MainActivity)
            adapter = mAdapter
        }
    }
}