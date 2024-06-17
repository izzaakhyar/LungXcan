package com.bangkit.lungxcan.ui.register

import androidx.lifecycle.ViewModel
import com.bangkit.lungxcan.data.repository.RegisterRepository

class RegisterViewModel(private val repository: RegisterRepository) : ViewModel() {
    fun createAccount(name: String, email: String, password: String) =
        repository.createAccount(name, email, password)
}