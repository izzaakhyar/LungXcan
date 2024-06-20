package com.bangkit.lungxcan.ui.result.articledisease

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.lungxcan.data.response.ArticleDiseaseResponseItem
import com.bangkit.lungxcan.databinding.ItemArticleDiseaseBinding

class ArticleDiseaseAdapter :
    ListAdapter<ArticleDiseaseResponseItem, ArticleDiseaseAdapter.ListViewHolder>(
        DIFF_CALLBACK
    ) {

    class ListViewHolder(private val binding: ItemArticleDiseaseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(articleItem: ArticleDiseaseResponseItem) {
            binding.tvArticleTitle.text = articleItem.title
            binding.tvArticleDesc.text = articleItem.summary
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemArticleDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ArticleWebviewActivity::class.java)
            intent.putExtra(ArticleWebviewActivity.ID_ARTICLE, article.url)
            holder.itemView.context.startActivity(intent)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ArticleDiseaseResponseItem> =
            object : DiffUtil.ItemCallback<ArticleDiseaseResponseItem>() {
                override fun areItemsTheSame(
                    oldItem: ArticleDiseaseResponseItem,
                    newItem: ArticleDiseaseResponseItem
                ): Boolean {
                    return oldItem.title == newItem.title
                }

                override fun areContentsTheSame(
                    oldItem: ArticleDiseaseResponseItem,
                    newItem: ArticleDiseaseResponseItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
