package com.bangkit.lungxcan.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.databinding.ActivityRegisterBinding
import com.bangkit.lungxcan.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupButton.setOnClickListener { setupAction() }
    }

    private fun setupAction() {
        val name = binding.edRegisterNama.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        viewModel.createAccount(name, email, password).observe(this) { result ->
            if (result != null) {
                if (result is ResultState.Loading) showLoading(true)
                if (result is ResultState.Success) {
                    showAlert(
                        "Success",
                        "User created successfully",
                        "Next",
                        startActivity(Intent(this, LoginActivity::class.java))
                    )
                    showLoading(false)
                } else if (result is ResultState.Error) {
                    showAlert("Error", result.error, "Try Again", closeOptionsMenu())
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showAlert(title: String, message: String, btnText: String, action: Unit) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(btnText) { _, _ ->
                action
            }
            create()
            show()
        }
    }
}