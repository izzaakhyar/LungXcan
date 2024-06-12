package com.bangkit.lungxcan

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.lungxcan.databinding.ActivityMainBinding
import com.bangkit.lungxcan.ui.setting.SettingFragment
import com.bangkit.lungxcan.ui.setting.SettingPreferences
import com.bangkit.lungxcan.ui.setting.SettingViewModel
import com.bangkit.lungxcan.ui.setting.SettingViewModelFactory
import com.bangkit.lungxcan.ui.setting.dataStore
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        val fromSet = SettingFragment.fromSetting

        if (!fromSet) {
            val isDarkModeActive = intent.getBooleanExtra("isDarkModeActive", false)

            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_history, R.id.navigation_scan, R.id.navigation_article, R.id.navigation_setting
//            )
//        )
//
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        supportActionBar?.title = when (View.NO_ID) {
            R.id.navigation_article -> getString(R.string.title_article)
            else -> {
                getString(R.string.app_name)
            }
        }

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        setThemeSetting(settingViewModel)
    }

    private fun setThemeSetting(settingViewModel: SettingViewModel) {
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}