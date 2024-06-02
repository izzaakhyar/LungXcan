package com.bangkit.lungxcan.ui.scan

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.databinding.FragmentScanBinding
import com.bangkit.lungxcan.databinding.ResultBottomSheetBinding
import com.bangkit.lungxcan.ui.result.ResultBottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.random.Random

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var resultBottomSheetBinding: ResultBottomSheetBinding

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

//        BottomSheetBehavior.from(resultBottomSheetBinding.standardBottomSheet).apply {
//            //peekHeight = 500
//            this.state = BottomSheetBehavior.STATE_EXPANDED
//        }

        binding.apply {
            btnCamera.setOnClickListener {
                startGallery()
                updateAnalyzeButtonState()
            }
            btnGallery.setOnClickListener {
                startCamera()
                updateAnalyzeButtonState()
            }
            btnAnalyze.setOnClickListener { analyzeImage() }

        }
        return root
    }

    private fun startGallery() {
        // TODO: Get image from Gallery.
    }

    private fun startCamera() {
        // TODO: Get image from Camera.
    }

    private fun showImage() {
        // TODO: Show image based on what selected from gallery/camera.
    }

    private fun analyzeImage() {
        // TODO: Analyzing chosen image.
        // Make progress circular visible
        binding.progressCircular.visibility = View.VISIBLE

        // Use a handler to delay the showing of the bottom sheet
        Handler(Looper.getMainLooper()).postDelayed({
            // Generate a random dummy result
            val randomDummyResult = Random.nextInt(0, 1)

            // Prepare and show the bottom sheet
            val resultBottomSheet = ResultBottomSheet()
            val bundle = Bundle().apply {
                putInt("result", randomDummyResult)
            }
            resultBottomSheet.arguments = bundle
            resultBottomSheet.show(childFragmentManager, ResultBottomSheet.TAG)

            // Hide the progress circular
            binding.progressCircular.visibility = View.GONE
        }, 3000) // Delay for 3 seconds
    }

    private fun updateAnalyzeButtonState() {
        binding.apply {
            btnAnalyze.isEnabled = true
            btnAnalyze.setBackgroundColor(resources.getColor(R.color.green))
            btnAnalyze.setTextColor(resources.getColor(R.color.white))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}