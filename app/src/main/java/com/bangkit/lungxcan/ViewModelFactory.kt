package com.bangkit.lungxcan

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.lungxcan.data.di.Injection
import com.bangkit.lungxcan.data.repository.ArticleRepository
import com.bangkit.lungxcan.data.repository.MapRepository
import com.bangkit.lungxcan.ui.article.ArticleViewModel
import com.bangkit.lungxcan.ui.result.HospitalViewModel

class ViewModelFactory(
    private val articleRepository: ArticleRepository,
    private val mapRepository: MapRepository
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

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideArticleRepository(),
                        Injection.provideMapRepository()
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}