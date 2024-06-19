package com.bangkit.lungxcan.ui.result.articledisease

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.databinding.ActivityArticleWebviewBinding

class ArticleWebviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleWebviewBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val url = intent.getStringExtra(ID_ARTICLE)
        if (url != null) {
            binding.webview.loadUrl(url)
        }
    }

    companion object {
        const val ID_ARTICLE = "id_article"
    }
}