package com.bangkit.lungxcan.ui.disease

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.lungxcan.data.repository.DiseaseRepository

class DiseaseViewModel(private val repository: DiseaseRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    fun getDiseaseDetail(idName: String) = repository.getDiseaseDetail(idName)
}