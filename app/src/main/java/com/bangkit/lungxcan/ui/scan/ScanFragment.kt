package com.bangkit.lungxcan.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.databinding.FragmentScanBinding

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val scanViewModel =
            ViewModelProvider(this)[ScanViewModel::class.java]

        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.appBar.topAppBar.title = getString(R.string.title_scan)

        binding.btnAnalyze.isEnabled = false

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