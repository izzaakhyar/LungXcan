package com.bangkit.lungxcan.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.lungxcan.MainActivity
import com.bangkit.lungxcan.R
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

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("isDarkModeActive", isDarkModeActive)
                startActivity(intent)
                finish()

            }, SPLASH_DELAY_TIME)
        }

    }

    companion object {
        private const val SPLASH_DELAY_TIME: Long = 2000
    }

}