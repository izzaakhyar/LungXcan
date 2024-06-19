package com.bangkit.lungxcan.ui.result.hospital

import androidx.lifecycle.ViewModel
import com.bangkit.lungxcan.data.repository.MapRepository

class HospitalViewModel(private val repository: MapRepository): ViewModel() {
    fun getHospital(location: String) = repository.getHospital(location)
}