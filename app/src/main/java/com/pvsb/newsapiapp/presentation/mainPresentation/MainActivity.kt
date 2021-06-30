package com.pvsb.newsapiapp.presentation.mainPresentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pvsb.newsapiapp.databinding.ActivityMainBinding
import com.pvsb.newsapiapp.presentation.mainPresentation.adapter.MainAdapter
import com.pvsb.newsapiapp.presentation.mainPresentation.adapter.NewsListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    private lateinit var binding : ActivityMainBinding

    private lateinit var mAdapter: MainAdapter

    private lateinit var mListener: NewsListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initiateAdapter()
        observeViewModel()
        setListener()
        initiateListener()


    }

    private fun initiateListener(){
        mAdapter.attachListener(mListener)

    }

    private fun initiateAdapter() {
        with(binding.recyclerViewMain){
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            mAdapter = MainAdapter(this@MainActivity)
            adapter = mAdapter
        }
    }

    private fun setListener(){
        mListener = object : NewsListener{
            override fun onClick() {
            }
        }
    }

    private fun observeViewModel() {

        viewModel.newsLiveData.observe(this, Observer { listNews ->
            mAdapter.getNews(listNews)
        })
        viewModel.getNewsViewModel()
    }
}