package com.bangkit.lungxcan.ui.result

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.data.HospitalRequest
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.response.ArticlesItem
import com.bangkit.lungxcan.databinding.ResultBottomSheetBinding
import com.bangkit.lungxcan.ui.article.ArticleAdapter
import com.bangkit.lungxcan.ui.article.ArticleViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ResultBottomSheet : BottomSheetDialogFragment() {

    private lateinit var _binding: ResultBottomSheetBinding
    private val binding get() = _binding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val articleViewModel by viewModels<ArticleViewModel> {
        ViewModelFactory.getInstance()
    }

    private val hospitalViewModel by viewModels<HospitalViewModel> {
        ViewModelFactory.getInstance()
    }

    private lateinit var hospitalData: HospitalRequest

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getLocation()

//        articleViewModel.getArticle()
//
//        articleViewModel.article.observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is ResultState.Loading -> {
//                    showLoading(true)
//                }
//
//                is ResultState.Success -> {
//                    showLoading(false)
//                    setArticleData(result.data)
//                }
//
//                is ResultState.Error -> {
//                    showLoading(false)
//                }
//            }
//        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    Log.d(TAG, "Precise location access granted.")
                    getLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    Log.d(TAG, "Approximate location access granted.")
                    getLocation()
                }

                else -> {
                    Log.d(TAG, "No location access granted.")
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val latLng = LatLng(location.latitude, location.longitude)
                    Log.d(TAG, "Location obtained: $latLng")
                    getNearbyHospitals(latLng)
                } ?: run {
                    Log.d(TAG, "Location is null")
                }
            }.addOnFailureListener { e ->
                Log.e(TAG, "Failed to get location", e)
            }
        } else {
            Log.d(TAG, "Requesting location permissions")
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun getNearbyHospitals(latLng: LatLng) {
        Log.d(TAG, "Fetching nearby hospitals for location: $latLng")
        hospitalViewModel.getHospital("${latLng.latitude},${latLng.longitude}")
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        Log.d(TAG, "Loading hospitals...")
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        Log.d(TAG, "Hospitals loaded successfully")
                        showLoading(false)
                        val results = result.data.results
                        val hospitalRequest = mutableListOf<HospitalRequest>()
                        for (index in results) {
                            for (photo in index.photos) {
                                hospitalData = HospitalRequest(
                                    index.name,
                                    index.vicinity,
                                    photo.photoReference
                                )
                                hospitalRequest.add(hospitalData)
                            }
                            setHospitalData(hospitalRequest)
                        }
                    }

                    is ResultState.Error -> {
                        Log.e(TAG, "Error loading hospitals: ${result.error}")
                        showLoading(false)
                    }
                }
            }
    }

    private fun setArticleData(result: List<ArticlesItem>) {
        val articleAdapter = ArticleAdapter()
        articleAdapter.submitList(result)
        binding.rvRecommend.adapter = articleAdapter
    }

    private fun setHospitalData(result: List<HospitalRequest>) {
        val hospitalAdapter = HospitalAdapter()
        hospitalAdapter.submitList(result)
        binding.rvRecommend.adapter = hospitalAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "ResultBottomSheet"
    }
}
