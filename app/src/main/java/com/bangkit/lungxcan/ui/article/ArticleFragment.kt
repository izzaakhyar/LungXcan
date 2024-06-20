package com.bangkit.lungxcan.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.response.ArticlesItem
import com.bangkit.lungxcan.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {

    private lateinit var _binding: FragmentArticleBinding
    private val binding get() = _binding

    private val articleViewModel by viewModels<ArticleViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        binding.appBar.topAppBar.title = getString(R.string.title_article)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvArticle.layoutManager = layoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeArticle()

        binding.btnTryAgain.setOnClickListener { observeArticle() }
    }

    private fun observeArticle() {
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
                    showError(result.error)
                    showErrorState()
                }
            }
        }

        articleViewModel.getArticle()
    }

    private fun showErrorState() {
        binding.progressBar.visibility = View.GONE
        binding.rvArticle.visibility = View.GONE
        binding.btnTryAgain.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setArticleData(result: List<ArticlesItem>) {
        val articleAdapter = ArticleAdapter()
        articleAdapter.submitList(result)
        binding.rvArticle.adapter = articleAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object
}