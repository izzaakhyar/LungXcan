package com.bangkit.lungxcan.ui.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {

    private lateinit var _binding: FragmentArticleBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        binding.appBar.topAppBar.title = getString(R.string.title_article)
        binding.appBar.topAppBar.isTitleCentered = true
        return binding.root
    }

    companion object {

    }
}