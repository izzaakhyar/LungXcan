package com.bangkit.lungxcan.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.bangkit.lungxcan.MainActivity
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.ui.login.LoginActivity
import com.bangkit.lungxcan.ui.login.LoginViewModel
import com.bangkit.lungxcan.ui.setting.SettingPreferences
import com.bangkit.lungxcan.ui.setting.SettingViewModel
import com.bangkit.lungxcan.ui.setting.SettingViewModelFactory
import com.bangkit.lungxcan.ui.setting.dataStore

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]
//
//        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
//            Handler(Looper.getMainLooper()).postDelayed({
//                val intent = Intent(this, MainActivity::class.java)
//                intent.putExtra("isDarkModeActive", isDarkModeActive)
//                startActivity(intent)
//                finish()
//
//            }, SPLASH_DELAY_TIME)
//        }

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )

            val authViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[LoginViewModel::class.java]
            authViewModel.getSession().observe(this) { user ->
                val nextActivity = if (user.isLogin) {
                    MainActivity::class.java
                } else {
                    LoginActivity::class.java
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, nextActivity))
                    finish()
                }, SPLASH_DELAY_TIME)
            }
        }

    }

    companion object {
        private const val SPLASH_DELAY_TIME: Long = 2000
    }

}