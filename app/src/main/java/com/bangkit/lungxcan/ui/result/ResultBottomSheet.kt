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
import com.bangkit.lungxcan.data.request.HospitalRequest
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.databinding.ResultBottomSheetBinding
import com.bangkit.lungxcan.ui.result.articledisease.ArticleDiseaseFragment
import com.bangkit.lungxcan.ui.article.ArticleViewModel
import com.bangkit.lungxcan.ui.disease.DiseaseViewModel
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

    private val articleViewModel by viewModels<ArticleViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val hospitalViewModel by viewModels<HospitalViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val diseaseViewModel by viewModels<DiseaseViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var hospitalData: HospitalRequest
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

//        if (position == 1) {
//            getLocation()
//        } else {
//            getLocation()
//        }

        diseaseViewModel.getDiseaseDetail(id.toString()).observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    //binding.tvResultDesc.text = result.data.detail
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

//                        builder.setTitle("Info")
//                        builder.setMessage(result.data.detail)
//                        builder.setPositiveButton("OK", null)
//                        builder.show()
                    }
                    //binding.diseaseDescriptionTextView.text = disease.description
                    showLoading(false)
                }
                is ResultState.Error -> {
//                    AlertDialog.Builder(this).apply {
//                        setTitle("Error")
//                        setMessage(result.error)
//                        setPositiveButton("Retry") { _, _ -> }
//                        create()
//                        show()
//                    }
                    //showLoading(false)
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

//    private val requestPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            when {
//                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
//                    Log.d(TAG, "Precise location access granted.")
//                    getLocation()
//                }
//
//                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
//                    Log.d(TAG, "Approximate location access granted.")
//                    getLocation()
//                }
//
//                else -> {
//                    Log.d(TAG, "No location access granted.")
//                }
//            }
//        }
//
//    private fun checkPermission(permission: String): Boolean {
//        return ContextCompat.checkSelfPermission(
//            requireActivity(),
//            permission
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun getLocation() {
//        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
//            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
//        ) {
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                location?.let {
//                    val latLng = LatLng(location.latitude, location.longitude)
//                    Log.d(TAG, "Location obtained: $latLng")
//                    getNearbyHospitals(latLng)
//                } ?: run {
//                    Log.d(TAG, "Location is null")
//                }
//            }.addOnFailureListener { e ->
//                Log.e(TAG, "Failed to get location", e)
//            }
//        } else {
//            Log.d(TAG, "Requesting location permissions")
//            requestPermissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//        }
//    }
//
//    private fun getNearbyHospitals(latLng: LatLng) {
//        Log.d(TAG, "Fetching nearby hospitals for location: $latLng")
//        hospitalViewModel.getHospital("${latLng.latitude},${latLng.longitude}")
//            .observe(viewLifecycleOwner) { result ->
//                when (result) {
//                    is ResultState.Loading -> {
//                        Log.d(TAG, "Loading hospitals...")
//                        showLoading(true)
//                    }
//
//                    is ResultState.Success -> {
//                        Log.d(TAG, "Hospitals loaded successfully")
//                        showLoading(false)
//                        val results = result.data.results
//                        val hospitalRequest = mutableListOf<HospitalRequest>()
//                        for (index in results) {
//                            for (photo in index.photos) {
//                                hospitalData = HospitalRequest(
//                                    index.name,
//                                    index.vicinity,
//                                    photo.photoReference
//                                )
//                                hospitalRequest.add(hospitalData)
//                            }
//                            setHospitalData(hospitalRequest)
//                        }
//                    }
//
//                    is ResultState.Error -> {
//                        Log.e(TAG, "Error loading hospitals: ${result.error}")
//                        showLoading(false)
//                    }
//                }
//            }
//    }
//
//    private fun setArticleData(result: List<ArticlesItem>) {
//        val articleAdapter = ArticleAdapter()
//        articleAdapter.submitList(result)
//        binding.rvRecommend.adapter = articleAdapter
//    }
//
//    private fun setHospitalData(result: List<HospitalRequest>) {
//        val hospitalAdapter = HospitalAdapter()
//        hospitalAdapter.submitList(result)
//        binding.rvRecommend.adapter = hospitalAdapter
//    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_TITLE = "title"

        const val TAG = "ResultBottomSheet"
    }
}
