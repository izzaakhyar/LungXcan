package com.bangkit.lungxcan.data.repository

import androidx.constraintlayout.motion.utils.ViewState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.api.ApiConfig
import com.bangkit.lungxcan.data.response.ArticleResponse
import com.bangkit.lungxcan.data.response.ArticlesItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleRepository {

    fun getArticle(data: MutableLiveData<ResultState<List<ArticlesItem>>>): LiveData<ResultState<List<ArticlesItem>>> {

        data.value = ResultState.Loading

        val client = ApiConfig.getApiService().getLungHealthArticle("Lung health", "publishedAt")
        client.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        data.value = ResultState.Success(responseBody.articles)
                    } else {
                        data.value = ResultState.Error("Empty response body")
                    }
                } else {
                    data.value = ResultState.Error(response.message() ?: "API call failed")
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                data.value = ResultState.Error(t.message ?: "Network error")
            }
        })

        return data
    }

    companion object {
        @Volatile
        private var instance: ArticleRepository? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ArticleRepository()
            }.also { instance = it }
    }
}