package com.pvsb.newsapiapp.presentation.detailsPresentation

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pvsb.newsapiapp.R
import com.pvsb.newsapiapp.databinding.ActivityDetailsBinding
import com.pvsb.newsapiapp.model.constants.AppConstants
import com.pvsb.newsapiapp.model.constants.AppConstants.lastMsg
import com.pvsb.newsapiapp.model.constants.AppConstants.msg
import java.io.File
import java.io.FileOutputStream
import java.util.jar.Manifest

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
                saveImage()
                true
            }
            R.id.action_share -> {
                bottomSheet()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        ) {
            askPermission()
        } else {
            val url = intent.getStringExtra(AppConstants.INTENT_URL_TO_IMAGE)
            val getUrl = url!!
            downloadImage(getUrl)
        }
    }

    private fun bottomSheet() {
        val url = intent.getStringExtra(AppConstants.INTENT_URL)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "$url")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun askPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("To save images you need this permission")
                    .setPositiveButton("Accept") { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                        finish()
                    }
                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            }

        } else {
            val url = intent.getStringExtra(AppConstants.INTENT_URL_TO_IMAGE)

            val getUrl = url!!
            downloadImage(getUrl)
        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val url = intent.getStringExtra(AppConstants.INTENT_URL_TO_IMAGE)
                    val getUrl = url!!
                    downloadImage(getUrl)
                }
            }
        }
    }

    private fun downloadImage(url: String) {

        val directory = File(Environment.DIRECTORY_DOWNLOADS)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or
                    DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url.substring(url.lastIndexOf("/") + 1)
                )
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) ==
                    DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url, directory, status)
                if (msg != lastMsg) {
                    this.runOnUiThread {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }.start()
    }

    private fun statusMessage(url: String, directory: File, status: Int): String? {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Fail, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded in $directory" + File.separator +
                    url.substring(
                url.lastIndexOf("/") + 1
            )
            else -> "Nothing to download"
        }
        return msg
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

