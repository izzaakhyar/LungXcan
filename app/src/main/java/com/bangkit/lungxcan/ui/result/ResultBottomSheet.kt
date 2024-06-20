package com.bangkit.lungxcan.ui.result

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.request.HospitalRequest
import com.bangkit.lungxcan.databinding.ResultBottomSheetBinding
import com.bangkit.lungxcan.ui.article.ArticleViewModel
import com.bangkit.lungxcan.ui.disease.DiseaseViewModel
import com.bangkit.lungxcan.ui.result.articledisease.ArticleDiseaseFragment
import com.bangkit.lungxcan.ui.result.hospital.HospitalViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.NumberFormat

class ResultBottomSheet : BottomSheetDialogFragment() {

    private lateinit var _binding: ResultBottomSheetBinding
    private val binding get() = _binding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val diseaseViewModel by viewModels<DiseaseViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var position = 0
    private var title = ""

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
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            title = it.getString(ARG_TITLE) ?: ""
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val id = requireArguments().getInt("id")
        val disease = requireArguments().getString("disease")
        val score = requireArguments().getFloat("score")

        val articleDisease = disease?.let { ArticleDiseaseFragment.newInstance(it) }

        binding.tvResultProbability.text = NumberFormat.getPercentInstance().format(score).trim()
        binding.tvDiagnose.text = disease

        val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        val adapter = disease?.let { PagerAdapter(requireActivity(), it) }
        viewPager.adapter = adapter
//        binding.tabLayout.setupWithViewPager(binding.viewPager)
        TabLayoutMediator(tabLayout, viewPager) { tabs, position ->
            tabs.text = when (position) {
                0 -> "Hospital"
                else -> "Article"
            }
        }.attach()

        diseaseViewModel.getDiseaseDetail(id.toString()).observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    binding.info.setOnClickListener {
                        val builder = AlertDialog.Builder(requireContext())
                        val inflater = layoutInflater
                        val dialogLayout = inflater.inflate(R.layout.custom_dialog, null)
                        val dialogMessage = dialogLayout.findViewById<TextView>(R.id.dialog_message)
                        dialogMessage.text = result.data.detail

                        builder.setView(dialogLayout)
                            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        val dialog = builder.create()
                        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_round)
                        dialog.show()

                    }
                    showLoading(false)
                }

                is ResultState.Error -> {

                }
            }
        }

        when {
            (score > 0.5 && disease != "NORMAL") || (score < 0.5 && disease == "NORMAL") -> {
                binding.apply {
                    tvResultProbability.setTextColor(resources.getColor(R.color.colorError))
                    tvResultDesc.text = getString(R.string.result_high)
                    tvRecommendTitle.text = getString(R.string.recommend_title_high)
                }
            }

            (score < 0.5 && disease != "NORMAL") || (score > 0.5 && disease == "NORMAL") -> {
                binding.apply {
                    tvResultProbability.setTextColor(resources.getColor(R.color.green))
                    tvResultDesc.text = getString(R.string.result_low)
                    tvRecommendTitle.text = getString(R.string.recommend_title_low)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_TITLE = "title"

        const val TAG = "ResultBottomSheet"
    }
}
