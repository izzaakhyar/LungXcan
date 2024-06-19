package com.bangkit.lungxcan.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.repository.ArticleDiseaseRepository
import com.bangkit.lungxcan.data.response.ArticleDiseaseResponseItem

class ArticleDiseaseViewModel(private val repository: ArticleDiseaseRepository) : ViewModel() {

    fun getArticleDiseaseDetail(disease: String): LiveData<ResultState<List<ArticleDiseaseResponseItem>>> = repository.getArticleDiseaseDetail(disease)
}