package com.pvsb.newsapiapp.presentation.mainPresentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pvsb.newsapiapp.R
import com.pvsb.newsapiapp.model.NewsEntity

class MainAdapter(context: Context): RecyclerView.Adapter<MainAdapter.NewsViewHolder>() {

    private var news: List<NewsEntity> = ArrayList()

    inner class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val newsImage = itemView.findViewById<ImageView>(R.id.news_image)
        private val newsTitle = itemView.findViewById<TextView>(R.id.news_title)

        fun onBind(newsEntity: NewsEntity){

            newsTitle.text = newsEntity.title

            Glide.with(itemView.context)
                .load(newsEntity.urlToImage)
                .into(newsImage)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return NewsViewHolder(view)

    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(news[position])
    }

    override fun getItemCount(): Int =  news.size

    fun getNews(news: List<NewsEntity>){
        this.news = news
        update()
    }

    private fun  update(){
        notifyDataSetChanged()
    }

}