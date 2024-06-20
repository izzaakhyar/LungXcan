package com.bangkit.lungxcan.ui.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.databinding.FragmentSettingBinding
import com.bangkit.lungxcan.ui.login.LoginViewModel

class SettingFragment : Fragment() {

    private lateinit var _binding: FragmentSettingBinding
    private val binding get() = _binding

    private val authViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    var token = ""
    var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding.appBar.topAppBar.title = getString(R.string.title_setting)
        authViewModel.user.observe(viewLifecycleOwner) {
            username = it.username
            binding.tvUsername.text = username
            binding.tvEmail.text = it.email
            Log.d("SettingFragment", "Username: $username")
        }
        authViewModel.getSession().observe(viewLifecycleOwner) {
            token = it.token
            Log.d("SettingFragment", "Token: $token")
            authViewModel.getUserInfo(token)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings()
            .observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    iconNight()
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    iconDay()
                }
                fromSetting = true
            }

        binding.switchTheme.setOnCheckedChangeListener { buttonView, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }

        binding.cardLogout.setOnClickListener {
            authViewModel.logout()
        }

        binding.cardLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

    }

    private fun iconDay() {
        binding.apply {
            switchTheme.isChecked = false
            ivProfile.setImageResource(R.drawable.account_24)
            iconLanguage.setImageResource(R.drawable.ic_language_24)
            iconTheme.setImageResource(R.drawable.ic_light_mode_24)
            iconLogout.setImageResource(R.drawable.ic_logout_24)
        }
    }

    private fun iconNight() {
        binding.apply {
            switchTheme.isChecked = true
            ivProfile.setImageResource(R.drawable.account_night)
            iconLanguage.setImageResource(R.drawable.ic_language_night)
            iconTheme.setImageResource(R.drawable.ic_nights_mode_24)
            iconLogout.setImageResource(R.drawable.ic_logout_night)
        }
    }

    companion object {
        var fromSetting = false
    }
}