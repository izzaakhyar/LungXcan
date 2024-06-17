package com.bangkit.lungxcan

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.lungxcan.data.di.Injection
import com.bangkit.lungxcan.data.repository.ArticleRepository
import com.bangkit.lungxcan.data.repository.LoginRepository
import com.bangkit.lungxcan.data.repository.MapRepository
import com.bangkit.lungxcan.data.repository.RegisterRepository
import com.bangkit.lungxcan.ui.article.ArticleViewModel
import com.bangkit.lungxcan.ui.login.LoginViewModel
import com.bangkit.lungxcan.ui.register.RegisterViewModel
import com.bangkit.lungxcan.ui.result.HospitalViewModel

class ViewModelFactory(
    private val articleRepository: ArticleRepository,
    private val mapRepository: MapRepository,
    private val loginRepository: LoginRepository,
    private val registerRepository: RegisterRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ArticleViewModel::class.java) -> {
                ArticleViewModel(articleRepository) as T
            }
            modelClass.isAssignableFrom(HospitalViewModel::class.java) -> {
                HospitalViewModel(mapRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(loginRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(registerRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideArticleRepository(),
                        Injection.provideMapRepository(),
                        Injection.provideLoginRepository(context),
                        Injection.provideRegisterRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}