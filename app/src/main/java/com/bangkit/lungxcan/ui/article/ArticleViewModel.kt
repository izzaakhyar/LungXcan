package com.bangkit.lungxcan.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.repository.ArticleRepository
import com.bangkit.lungxcan.data.response.ArticlesItem

class ArticleViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val _article = MutableLiveData<ResultState<List<ArticlesItem>>>()
    val article: LiveData<ResultState<List<ArticlesItem>>> = _article

    fun getArticle() {
        repository.getArticle(_article)
    }
}