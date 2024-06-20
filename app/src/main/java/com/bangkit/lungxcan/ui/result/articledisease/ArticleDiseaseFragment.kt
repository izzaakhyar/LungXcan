package com.bangkit.lungxcan.ui.result.articledisease

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.response.ArticleDiseaseResponseItem
import com.bangkit.lungxcan.databinding.FragmentArticleDiseaseBinding

class ArticleDiseaseFragment : Fragment() {

    private lateinit var _binding: FragmentArticleDiseaseBinding

    private val binding get() = _binding

    private val viewModel by viewModels<ArticleDiseaseViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleDiseaseBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvArticle.layoutManager = layoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val disease = arguments?.getString(ARG_DISEASE)?.replace(" ", "_")

        if (disease != null) {
            Log.d(TAG, disease)
        }

        if (disease != null) {
            if (disease == "NORMAL") {
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                observeArticleDiseaseDetail(disease)
            }
        }

        binding.btnTryAgain.setOnClickListener {
            if (disease != null) {
                observeArticleDiseaseDetail(disease)
            }
        }
    }

    private fun observeArticleDiseaseDetail(disease: String) {
        viewModel.getArticleDiseaseDetail(disease.lowercase())
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> {
                        setArticleDiseaseData(result.data)
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        showError(result.error)
                        showErrorState()
                    }
                }
            }
    }

    private fun setArticleDiseaseData(result: List<ArticleDiseaseResponseItem>) {
        val articleDiseaseAdapter = ArticleDiseaseAdapter()
        articleDiseaseAdapter.submitList(result)
        binding.rvArticle.adapter = articleDiseaseAdapter
        binding.progressBar.visibility = View.GONE
        binding.btnTryAgain.visibility = View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvArticle.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.btnTryAgain.visibility = View.GONE
    }

    private fun showErrorState() {
        binding.progressBar.visibility = View.GONE
        binding.rvArticle.visibility = View.GONE
        binding.btnTryAgain.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {

        const val TAG = "ArticleDiseaseFragmeny"
        private const val ARG_DISEASE = "disease"

        fun newInstance(disease: String): ArticleDiseaseFragment {
            val fragment = ArticleDiseaseFragment()
            val args = Bundle()
            args.putString(ARG_DISEASE, disease)
            fragment.arguments = args
            return fragment
        }
    }
}