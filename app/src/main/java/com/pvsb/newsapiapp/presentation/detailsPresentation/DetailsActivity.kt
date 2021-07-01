package com.pvsb.newsapiapp.presentation.detailsPresentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pvsb.newsapiapp.R
import com.pvsb.newsapiapp.databinding.ActivityDetailsBinding
import com.pvsb.newsapiapp.model.constants.AppConstants
import java.io.File
import java.io.FileOutputStream

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setExtras()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {

                true
            }
            R.id.action_share -> {

                val url = intent.getStringExtra(AppConstants.INTENT_URL)

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "$url")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setExtras() {
        binding.newsDetailsAuthor.text = intent.getStringExtra(AppConstants.INTENT_AUTHOR)
        binding.newsDetailsTitle.text = intent.getStringExtra(AppConstants.INTENT_TITLE)
        binding.newsDetailsDescription.text = intent.getStringExtra(AppConstants.INTENT_DESCRIPTION)
        binding.newsDetailsContent.text = intent.getStringExtra(AppConstants.INTENT_CONTENT)
        binding.newsDetailsUrl.text = intent.getStringExtra(AppConstants.INTENT_URL)
        binding.newsDetailsPublishedAt.text = intent.getStringExtra(AppConstants.INTENT_PUBLISHED_AT)

        val newsImage = intent.getStringExtra(AppConstants.INTENT_URL_TO_IMAGE)
        Glide.with(this).load(newsImage)
            .centerCrop()
            .into(binding.newsDetailsImage)
    }
}

