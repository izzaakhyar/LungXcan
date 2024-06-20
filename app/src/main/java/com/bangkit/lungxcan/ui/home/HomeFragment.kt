package com.bangkit.lungxcan.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.request.DiseaseRequest
import com.bangkit.lungxcan.data.response.ArticlesItem
import com.bangkit.lungxcan.databinding.FragmentHomeBinding
import com.bangkit.lungxcan.ui.article.ArticleViewModel
import com.bangkit.lungxcan.ui.disease.DiseaseAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val list = ArrayList<DiseaseRequest>()

    private val binding get() = _binding!!

    private val articleViewModel by viewModels<ArticleViewModel> {
        ViewModelFactory.getInstance(requireContext())
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

        val articleLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val diseaseLayoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvArticle.layoutManager = articleLayoutManager
        binding.rvDisease.layoutManager = diseaseLayoutManager

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

        list.addAll(getListDisease())

        val diseaseAdapter = DiseaseAdapter(list)
        diseaseAdapter.submitList(list.take(4))
        binding.rvDisease.adapter = diseaseAdapter
    }

    private fun setArticleData(result: List<ArticlesItem>) {
        val articleAdapter = ArticleHomeAdapter()
        articleAdapter.submitList(result)
        binding.rvArticle.adapter = articleAdapter
    }

    private fun getListDisease(): ArrayList<DiseaseRequest> {
        val dataDiseaseNames = resources.getStringArray(R.array.disease_names)
        val dataDiseasePictures = resources.getStringArray(R.array.disease_pictures)
        val dataDiseaseAvailability = resources.getStringArray(R.array.disease_availability)
        val dataDiseaseLinks = resources.getStringArray(R.array.disease_links)
        val dataDiseaseDefinitions = resources.getStringArray(R.array.disease_definitions)
        val dataDiseaseSymptoms = resources.getStringArray(R.array.disease_symptoms)
        val dataDiseaseTreatments = resources.getStringArray(R.array.disease_treatments)
        val dataDiseasePreventions = resources.getStringArray(R.array.disease_preventions)
        val dataDiseaseIcons = resources.getStringArray(R.array.disease_icons)

        val diseaseList = ArrayList<DiseaseRequest>()

        for (i in dataDiseaseNames.indices) {
            val disease = DiseaseRequest(
                disease = dataDiseaseNames[i],
                image = dataDiseasePictures[i],
                availability = dataDiseaseAvailability[i],
                linkRead = dataDiseaseLinks[i],
                definition = dataDiseaseDefinitions[i],
                symptom = dataDiseaseSymptoms[i],
                treatment = dataDiseaseTreatments[i],
                preventive = dataDiseasePreventions[i],
                diseaseIcon = dataDiseaseIcons[i]
            )
            diseaseList.add(disease)
        }

        return diseaseList
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}