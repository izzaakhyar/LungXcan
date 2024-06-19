package com.bangkit.lungxcan.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.databinding.FragmentScanBinding
import com.bangkit.lungxcan.databinding.ResultBottomSheetBinding
//import com.bangkit.lungxcan.helper.ImageClassificationHelper
import com.bangkit.lungxcan.helper.ImageClassifierHelper
//import com.bangkit.lungxcan.helper.ImageClassifierHelper
import com.bangkit.lungxcan.utils.getImageUri
import com.bangkit.lungxcan.ui.result.ResultBottomSheet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.task.vision.classifier.Classifications
//import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat
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
            btnAnalyze.setOnClickListener {
                currentImageUri?.let {
                    binding.progressCircular.visibility = View.VISIBLE
                    analyzeImage(it)
                } ?: run {
                    showToast(getString(R.string.analyze)) // change later
                }
            }

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

    private fun analyzeImage(uri: Uri) {
        binding.progressCircular.visibility = View.VISIBLE

        lifecycleScope.launch {
            val helper = ImageClassifierHelper(
                context = requireContext(),
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        lifecycleScope.launch(Dispatchers.Main) {
                            binding.progressCircular.visibility = View.GONE
                            showToast(error)
                        }
                    }

                    override fun onResults(results: List<ImageClassifierHelper.Classifications>?) {
                        lifecycleScope.launch(Dispatchers.Main) {
                            results?.let {
                                if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                    val sortedCategories = it[0].categories.sortedByDescending { it.score }

                                    val resultBottomSheet = ResultBottomSheet()
                                    val bundle = Bundle()
                                    val idDisease = getIdDisease(sortedCategories[0].label)
                                    bundle.putInt("id", idDisease)
                                    bundle.putString("disease", sortedCategories[0].label)
                                    bundle.putFloat("score", sortedCategories[0].score)
                                    resultBottomSheet.arguments = bundle
                                    resultBottomSheet.show(childFragmentManager, ResultBottomSheet.TAG)
                                }

                            }
                            // Hide the progress bar when the results are ready
                            binding.progressCircular.visibility = View.GONE


                        }
                    }

                })

            withContext(Dispatchers.IO) {
                helper.classifyStaticImage(uri)
            }
        }



//        val helper = ImageClassifierHelper(
//            context = requireContext(),
//            classifierListener = object : ImageClassifierHelper.ClassifierListener {
//                override fun onError(error: String) {
//                    showToast(error)
//                }
//
//                override fun onResults(results: List<Classifications>?) {
//                    activity?.runOnUiThread {
//                        results?.let { it ->
//                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
//                                println(it)
//                                val sortedCategories =
//                                    it[0].categories.sortedByDescending { it?.score }
//                                val displayResult =
//                                    "${
//                                        NumberFormat.getPercentInstance()
//                                            .format(sortedCategories[0].score).trim()
//                                    } " + sortedCategories[0].label
//                                //moveToResult(uri, displayResult)
//                                println(displayResult)
//                            }
//                        }
//                    }
//                }
//            })
//        helper.classifyStaticImage(uri)
    }


    private fun updateAnalyzeButtonState() {
        if (currentImageUri != null) {
            binding.btnAnalyze.isEnabled = true
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    binding.apply {
                        btnAnalyze.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                        btnAnalyze.setTextColor(resources.getColor(R.color.colorOnPrimary))
                    }
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    binding.apply {
                        btnAnalyze.setBackgroundColor(resources.getColor(R.color.green))
                        btnAnalyze.setTextColor(resources.getColor(R.color.white))
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getIdDisease(disease: String): Int {
        val labels = listOf(
            "CANCER",
            "COVID",
            "FIBROSIS",
            "NORMAL",
            "PLEURAL THICKENING",
            "PNEUMONIA",
            "TBC"
        )
        return labels.indexOf(disease) + 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUIRED_CAMERA_PERMISSION = Manifest.permission.CAMERA
    }
}