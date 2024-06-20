package com.bangkit.lungxcan

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.lungxcan.databinding.ActivityMainBinding
import com.bangkit.lungxcan.ui.login.LoginActivity
import com.bangkit.lungxcan.ui.login.LoginViewModel
import com.bangkit.lungxcan.ui.setting.SettingPreferences
import com.bangkit.lungxcan.ui.setting.SettingViewModel
import com.bangkit.lungxcan.ui.setting.SettingViewModelFactory
import com.bangkit.lungxcan.ui.setting.dataStore
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val authViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        observeSession()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTheme()

        setupNavigation()
    }

    private fun setupTheme() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun observeSession() {
        authViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else token = user.token
        }
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }
}
