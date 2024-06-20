package com.bangkit.lungxcan

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.lungxcan.data.di.Injection
import com.bangkit.lungxcan.data.repository.ArticleDiseaseRepository
import com.bangkit.lungxcan.data.repository.ArticleRepository
import com.bangkit.lungxcan.data.repository.DiseaseRepository
import com.bangkit.lungxcan.data.repository.LoginRepository
import com.bangkit.lungxcan.data.repository.MapRepository
import com.bangkit.lungxcan.data.repository.RegisterRepository
import com.bangkit.lungxcan.data.repository.UserRepository
import com.bangkit.lungxcan.ui.article.ArticleViewModel
import com.bangkit.lungxcan.ui.disease.DiseaseViewModel
import com.bangkit.lungxcan.ui.login.LoginViewModel
import com.bangkit.lungxcan.ui.register.RegisterViewModel
import com.bangkit.lungxcan.ui.result.articledisease.ArticleDiseaseViewModel
import com.bangkit.lungxcan.ui.result.hospital.HospitalViewModel

class ViewModelFactory(
    private val articleRepository: ArticleRepository,
    private val mapRepository: MapRepository,
    private val loginRepository: LoginRepository,
    private val registerRepository: RegisterRepository,
    private val userRepository: UserRepository,
    private val diseaseRepository: DiseaseRepository,
    private val articleDiseaseRepository: ArticleDiseaseRepository
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
                LoginViewModel(loginRepository, userRepository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(registerRepository) as T
            }

            modelClass.isAssignableFrom(DiseaseViewModel::class.java) -> {
                DiseaseViewModel(diseaseRepository) as T
            }

            modelClass.isAssignableFrom(ArticleDiseaseViewModel::class.java) -> {
                ArticleDiseaseViewModel(articleDiseaseRepository) as T
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
                        Injection.provideRegisterRepository(context),
                        Injection.provideUserRepository(context),
                        Injection.provideDiseaseRepository(),
                        Injection.provideArticleDiseaseRepository()
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}