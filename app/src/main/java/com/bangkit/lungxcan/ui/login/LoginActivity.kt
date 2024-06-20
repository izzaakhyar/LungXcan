package com.bangkit.lungxcan.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.lungxcan.MainActivity
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.pref.UserModel
import com.bangkit.lungxcan.databinding.ActivityLoginBinding
import com.bangkit.lungxcan.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener { setupAction() }
        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModel.login(email, password).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> handleLoginSuccess(result.data.token)
                    is ResultState.Error -> showError(result.error)
                }
            }
        } else {
            showAlert("Input Error", "Please enter both email and password")
        }
    }

    private fun handleLoginSuccess(token: String) {
        viewModel.saveSession(UserModel(token, true))
        showAlert("Success", "Login successful") {
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish()
        }
    }

    private fun showError(error: String) {
        showAlert("Error", error)
        showLoading(false)
    }

    private fun showAlert(title: String, message: String, onPositive: (() -> Unit)? = null) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { _, _ -> onPositive?.invoke() }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
