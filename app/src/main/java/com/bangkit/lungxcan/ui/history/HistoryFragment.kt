package com.bangkit.lungxcan.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.data.DummyHistory
import com.bangkit.lungxcan.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var _binding: FragmentHistoryBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val historyViewModel =
            ViewModelProvider(this)[HistoryViewModel::class.java]

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

//        val textView: TextView = binding.textDashboard
//        historyViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHistory.layoutManager = layoutManager

        binding.appBar.topAppBar.title = getString(R.string.title_history)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val histories = mutableListOf<DummyHistory>()
//        for (i in 0..10) {
//            val data = DummyHistory(80, "Lung Cancer", "24 Juni 2024")
//            histories.add(data)
//        }

        if (histories.isEmpty()) {
            binding.rvHistory.visibility = View.GONE
            binding.tvEmptyText.visibility = View.VISIBLE
        } else {
            val historyAdapter = HistoryAdapter()
            historyAdapter.submitList(histories)
            binding.rvHistory.adapter = historyAdapter
        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}