package com.bangkit.lungxcan.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.lungxcan.data.pref.UserModel
import com.bangkit.lungxcan.data.repository.LoginRepository
import com.bangkit.lungxcan.data.repository.UserRepository
import com.bangkit.lungxcan.data.response.User
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    fun login(email: String, password: String) =
        repository.signIn(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun getUserInfo(token: String) {
        viewModelScope.launch {
            userRepository.getUserInfo(token, _user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}