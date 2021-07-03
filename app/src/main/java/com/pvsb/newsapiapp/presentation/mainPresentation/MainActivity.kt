package com.pvsb.newsapiapp.presentation.mainPresentation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pvsb.newsapiapp.R
import com.pvsb.newsapiapp.databinding.ActivityMainBinding
import com.pvsb.newsapiapp.model.constants.AppConstants
import com.pvsb.newsapiapp.presentation.detailsPresentation.DetailsActivity
import com.pvsb.newsapiapp.presentation.mainPresentation.adapter.MainAdapter
import com.pvsb.newsapiapp.presentation.mainPresentation.adapter.NewsListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
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
        refreshPage()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_change_theme -> {

                val builder = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.change_theme))

                val styles = arrayOf("Light", "Dark")
                val checkedItem = 0

                builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->

                    when (which) {
                        0 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }
                        1 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }
                    }
                }

                val dialog = builder.create()
                dialog.show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun refreshPage() {
        binding.refreshLayout.setOnRefreshListener {
            Toast.makeText(this, "Page refreshed", Toast.LENGTH_SHORT).show()
            binding.refreshLayout.isRefreshing = false
            viewModel.getNewsViewModel()
        }
    }

    private fun initiateListener() {
        mAdapter.attachListener(mListener)
    }

    private fun initiateAdapter() {
        with(binding.recyclerViewMain) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            mAdapter = MainAdapter(this@MainActivity)
            adapter = mAdapter
        }
    }

    private fun setListener() {
        mListener = object : NewsListener {
            override fun onClick(position: Int) {
                if (mAdapter.getData().isNotEmpty()) {
                    val searchItem = mAdapter.getData()[position]
                    searchItem.let { news ->
                        val intent = Intent(applicationContext, DetailsActivity::class.java)
                        intent.putExtra(AppConstants.INTENT_AUTHOR, news.author)
                        intent.putExtra(AppConstants.INTENT_TITLE, news.title)
                        intent.putExtra(AppConstants.INTENT_DESCRIPTION, news.description)
                        intent.putExtra(AppConstants.INTENT_URL, news.url)
                        intent.putExtra(AppConstants.INTENT_URL_TO_IMAGE, news.urlToImage)
                        intent.putExtra(AppConstants.INTENT_CONTENT, news.content)
                        startActivity(intent)
                    }
                }
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