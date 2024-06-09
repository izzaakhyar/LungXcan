package com.bangkit.lungxcan.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.bangkit.lungxcan.ui.setting.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel (private val pref: SettingPreferences) : ViewModel() {

    private val _languages = MutableLiveData<Array<String>>()
    val languages: LiveData<Array<String>> = _languages

    init {
        _languages.value = arrayOf("English", "Indonesia", "Spanish", "France")
    }

    fun getThemeSettings() : LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}