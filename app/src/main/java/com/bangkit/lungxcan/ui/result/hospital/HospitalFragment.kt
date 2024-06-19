package com.bangkit.lungxcan.ui.result.hospital

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.data.request.HospitalRequest
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.databinding.FragmentHospitalBinding
import com.bangkit.lungxcan.ui.result.ResultBottomSheet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class HospitalFragment : Fragment() {

    private lateinit var _binding: FragmentHospitalBinding
    private val binding get() = _binding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val hospitalViewModel by viewModels<HospitalViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var hospitalData: HospitalRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHospitalBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHospital.layoutManager = layoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    Log.d(ResultBottomSheet.TAG, "Precise location access granted.")
                    getLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    Log.d(ResultBottomSheet.TAG, "Approximate location access granted.")
                    getLocation()
                }

                else -> {
                    Log.d(ResultBottomSheet.TAG, "No location access granted.")
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
                    Log.d(ResultBottomSheet.TAG, "Location obtained: $latLng")
                    getNearbyHospitals(latLng)
                } ?: run {
                    Log.d(ResultBottomSheet.TAG, "Location is null")
                }
            }.addOnFailureListener { e ->
                Log.e(ResultBottomSheet.TAG, "Failed to get location", e)
            }
        } else {
            Log.d(ResultBottomSheet.TAG, "Requesting location permissions")
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun getNearbyHospitals(latLng: LatLng) {
        Log.d(ResultBottomSheet.TAG, "Fetching nearby hospitals for location: $latLng")
        hospitalViewModel.getHospital("${latLng.latitude},${latLng.longitude}")
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        Log.d(ResultBottomSheet.TAG, "Loading hospitals...")
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        Log.d(ResultBottomSheet.TAG, "Hospitals loaded successfully")
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
                        Log.e(ResultBottomSheet.TAG, "Error loading hospitals: ${result.error}")
                        showLoading(false)
                    }
                }
            }
    }

    private fun setHospitalData(result: List<HospitalRequest>) {
        val hospitalAdapter = HospitalAdapter()
        hospitalAdapter.submitList(result)
        binding.rvHospital.adapter = hospitalAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {

    }
}