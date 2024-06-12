package com.bangkit.lungxcan.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.data.DummyHistory
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.response.ArticlesItem
import com.bangkit.lungxcan.databinding.FragmentHomeBinding
import com.bangkit.lungxcan.ui.article.ArticleAdapter
import com.bangkit.lungxcan.ui.article.ArticleViewModel
import com.bangkit.lungxcan.ui.history.HistoryAdapter
import com.bangkit.lungxcan.ui.history.HistoryFragment
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val articleViewModel by viewModels<ArticleViewModel> {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val articleLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val historyLayoutManager = LinearLayoutManager(requireActivity())
        binding.rvArticle.layoutManager = articleLayoutManager
        binding.rvScanHistory.layoutManager = historyLayoutManager

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        articleViewModel.getArticle()

        val histories = mutableListOf<DummyHistory>()
//        for (i in 0..10) {
//            val data = DummyHistory(80, "Lung Cancer", "24 Juni 2024")
//            histories.add(data)
//        }

        if (histories.isEmpty()) {
            binding.rvScanHistory.visibility = View.GONE
            binding.tvEmptyText.visibility = View.VISIBLE
        } else {
            val historyAdapter = HistoryAdapter()
            historyAdapter.submitList(histories)
            binding.rvScanHistory.adapter = historyAdapter
        }
    }

    private fun setArticleData(result: List<ArticlesItem>) {
        val articleAdapter = ArticleHomeAdapter()
        articleAdapter.submitList(result)
        binding.rvArticle.adapter = articleAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}