package com.bangkit.lungxcan.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.databinding.FragmentSettingBinding
import com.bangkit.lungxcan.ui.login.LoginViewModel
import com.google.android.material.textfield.MaterialAutoCompleteTextView

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
            //binding.tvtest.text = username
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
                    binding.switchTheme.isChecked = true
                    binding.iconTheme.setImageResource(R.drawable.ic_nights_mode_24)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.switchTheme.isChecked = false
                    binding.iconTheme.setImageResource(R.drawable.ic_light_mode_24)
                    binding.iconLogout.setColorFilter(R.color.black)
                }
                fromSetting = true
            }

        binding.switchTheme.setOnCheckedChangeListener { buttonView, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }

        settingViewModel.languages.observe(viewLifecycleOwner) { items ->
            (binding.autoComplete as? MaterialAutoCompleteTextView)?.setSimpleItems(items)
        }

        binding.cardLogout.setOnClickListener {
            authViewModel.logout()
        }

        //binding.tvtest.text = username

//        print(token)
//        print(username)

    }

    companion object {
        var fromSetting = false
    }
}