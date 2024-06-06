package com.bangkit.lungxcan.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.response.ArticlesItem
import com.bangkit.lungxcan.databinding.ResultBottomSheetBinding
import com.bangkit.lungxcan.ui.article.ArticleAdapter
import com.bangkit.lungxcan.ui.article.ArticleViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ResultBottomSheet : BottomSheetDialogFragment() {

    private lateinit var _binding: ResultBottomSheetBinding
    private val binding get() = _binding

    private val articleViewModel by viewModels<ArticleViewModel> {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ResultBottomSheetBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvRecommend.layoutManager = layoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        articleViewModel.getArticle()

        articleViewModel.article.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    showLoading(false)
                    setArticleData(result.data)
                }

                is ResultState.Error -> {
                    showLoading(false)
                }
            }
        }

//        val hospitals = mutableListOf<DummyHospital>()
//        for (i in 0..10) {
//            val hospital = DummyHospital("Rumah sakit $i", 0, "Jarak $i km")
//            hospitals.add(hospital)
//        }
//
//        val articles = mutableListOf<DummyArticle>()
//        for (i in 0..10) {
//            val article = DummyArticle("Judul article dummy $i")
//            articles.add(article)
//        }
//
//        val result = requireArguments().getInt("result")
//        val hospitalAdapter = HospitalAdapter()
//        val articleAdapter = ArticleAdapter()
//
//        if (result == 1) {
//            hospitalAdapter.submitList(hospitals)
//            binding.rvRecommend.adapter = hospitalAdapter
//        } else {
//            articleViewModel.article.observe(viewLifecycleOwner) {
//                articleAdapter.submitList(it)
//                binding.rvRecommend.adapter = articleAdapter
//            }
//            //articleAdapter.submitList(articles)
//
//        }
    }

    private fun setArticleData(result: List<ArticlesItem>) {
        val articleAdapter = ArticleAdapter()
        articleAdapter.submitList(result)
        binding.rvRecommend.adapter = articleAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "ResultBottomSheet"
    }
}