package com.bangkit.lungxcan.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.databinding.FragmentScanBinding
import com.bangkit.lungxcan.databinding.ResultBottomSheetBinding
import com.bangkit.lungxcan.utils.getImageUri
import com.bangkit.lungxcan.ui.result.ResultBottomSheet
import kotlin.random.Random

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private lateinit var resultBottomSheetBinding: ResultBottomSheetBinding

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
                if (pendingAction == Action.CAMERA) {
                    startCamera()
                }
            } else {
                showToast("Permission request denied")
            }
            pendingAction = null
        }

    private var pendingAction: Action? = Action.CAMERA

    private enum class Action {
        CAMERA
    }

    private fun allPermissionsGranted(permission: String) =
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val scanViewModel =
            ViewModelProvider(this)[ScanViewModel::class.java]

        resultBottomSheetBinding = ResultBottomSheetBinding.inflate(inflater, container, false)

        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.appBar.topAppBar.title = getString(R.string.title_scan)

        binding.btnAnalyze.isEnabled = false

        binding.apply {
            btnCamera.setOnClickListener {
                if (allPermissionsGranted(REQUIRED_CAMERA_PERMISSION)) {
                    startCamera()
                } else {
                    pendingAction = Action.CAMERA
                    requestPermissionLauncher.launch(REQUIRED_CAMERA_PERMISSION)
                }
            }
            btnGallery.setOnClickListener {
                startGallery()
            }
            btnAnalyze.setOnClickListener { analyzeImage() }

        }

        return root
    }

    private fun startGallery() {
        // TODO: Get image from Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        // TODO: Get image from Camera.
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        // TODO: Show image based on what selected from gallery/camera.
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivLungImage.setImageURI(it)
            updateAnalyzeButtonState()
        }
    }

    private fun analyzeImage() {
        // TODO: Analyzing chosen image.
        binding.progressCircular.visibility = View.VISIBLE

        val resultBottomSheet = ResultBottomSheet()
        resultBottomSheet.show(childFragmentManager, ResultBottomSheet.TAG)

        val randomDummyResult = Random.nextInt(0, 1)
        // Use a handler to delay the showing of the bottom sheet
//        Handler(Looper.getMainLooper()).postDelayed({
//            // Generate a random dummy result
//
//
//            // Prepare and show the bottom sheet
//            val resultBottomSheet = ResultBottomSheet()
////            val bundle = Bundle().apply {
////                putInt("result", randomDummyResult)
////            }
//            resultBottomSheet.arguments = bundle
//            resultBottomSheet.show(childFragmentManager, ResultBottomSheet.TAG)
//
//            // Hide the progress circular
//            binding.progressCircular.visibility = View.GONE
//        }, 3000) // Delay for 3 seconds
    }

    private fun updateAnalyzeButtonState() {
        if (currentImageUri != null) {
            binding.apply {
                btnAnalyze.isEnabled = true
                btnAnalyze.setBackgroundColor(resources.getColor(R.color.green))
                btnAnalyze.setTextColor(resources.getColor(R.color.white))
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUIRED_CAMERA_PERMISSION = Manifest.permission.CAMERA
    }
}