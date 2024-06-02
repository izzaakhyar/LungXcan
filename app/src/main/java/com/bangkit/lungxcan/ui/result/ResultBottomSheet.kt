package com.bangkit.lungxcan.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.lungxcan.data.DummyArticle
import com.bangkit.lungxcan.data.DummyHospital
import com.bangkit.lungxcan.databinding.ResultBottomSheetBinding
import com.bangkit.lungxcan.ui.article.ArticleAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ResultBottomSheet : BottomSheetDialogFragment() {

    private lateinit var _binding: ResultBottomSheetBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ResultBottomSheetBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHospital.layoutManager = layoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hospitals = mutableListOf<DummyHospital>()
        for (i in 0..10) {
            val hospital = DummyHospital("Rumah sakit $i", 0, "Jarak $i km")
            hospitals.add(hospital)
        }

        val articles = mutableListOf<DummyArticle>()
        for (i in 0..10) {
            val article = DummyArticle("Judul article dummy $i")
            articles.add(article)
        }

        val result = requireArguments().getInt("result")
        val hospitalAdapter = HospitalAdapter()
        val articleAdapter = ArticleAdapter()

        if (result == 1) {
            hospitalAdapter.submitList(hospitals)
            binding.rvHospital.adapter = hospitalAdapter
        } else {
            articleAdapter.submitList(articles)
            binding.rvHospital.adapter = articleAdapter
        }
    }

    companion object {
        const val TAG = "ResultBottomSheet"
    }
}