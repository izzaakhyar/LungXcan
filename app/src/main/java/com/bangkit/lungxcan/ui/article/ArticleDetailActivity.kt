package com.bangkit.lungxcan.ui.article

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.data.response.ArticlesItem
import com.bangkit.lungxcan.databinding.ActivityArticleDetailBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ArticleDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val detail = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(ID_ARTICLE, ArticlesItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ID_ARTICLE)
        }

        if (detail != null) {
            binding.apply {
                tvContentDate.text = detail.publishedAt
                tvContentTitle.text = detail.title
                tvAuthor.text = detail.author
                shareArticle.setOnClickListener {
                    detail.url?.let { it1 -> shareLink(it1) }
                }
                Glide.with(this@ArticleDetailActivity)
                    .load(detail.urlToImage)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error_24)
                    )
                    .into(ivContentImage)
                tvArticleContent.text = detail.content
            }
        }

        binding.apply {
            btnSeeMore.setOnClickListener {
                webview.visibility = View.VISIBLE
                if (detail != null) {
                    detail.url?.let { it1 -> webview.loadUrl(it1) }
                }
            }
        }

    }

    private fun shareLink(url: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    companion object {
        const val ID_ARTICLE = "id_article"
    }
}