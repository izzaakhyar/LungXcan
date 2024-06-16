package com.bangkit.lungxcan.ui.disease

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.data.DummyDisease
import com.bangkit.lungxcan.databinding.FragmentDiseaseBinding

class DiseaseFragment : Fragment() {

    private lateinit var _binding: FragmentDiseaseBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val diseaseViewModel =
            ViewModelProvider(this)[DiseaseViewModel::class.java]

        _binding = FragmentDiseaseBinding.inflate(inflater, container, false)

//        val textView: TextView = binding.textDashboard
//        historyViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvHistory.layoutManager = layoutManager

        binding.appBar.topAppBar.title = getString(R.string.title_disease)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val histories = mutableListOf<DummyDisease>()
        for (i in 0..9) {
            val data = DummyDisease(80, "Lung Cancer", "24 Juni 2024")
            histories.add(data)
        }

        if (histories.isEmpty()) {
            binding.rvHistory.visibility = View.GONE
            binding.tvEmptyText.visibility = View.VISIBLE
        } else {
            val diseaseAdapter = DiseaseAdapter()
            diseaseAdapter.submitList(histories)
            binding.rvHistory.adapter = diseaseAdapter
        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}