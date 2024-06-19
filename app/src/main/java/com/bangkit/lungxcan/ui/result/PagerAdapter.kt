package com.bangkit.lungxcan.ui.result

import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.lungxcan.ViewModelFactory
import com.bangkit.lungxcan.ui.article.ArticleDiseaseFragment
import com.bangkit.lungxcan.ui.article.ArticleFragment
import com.bangkit.lungxcan.ui.article.ArticleViewModel
import com.bangkit.lungxcan.ui.disease.DiseaseFragment


class PagerAdapter(fragment: FragmentActivity, private val disease: String) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var title = ""
        val fragment = ResultBottomSheet()
        fragment.arguments = Bundle().apply {
            putInt(ResultBottomSheet.ARG_POSITION, position + 1)
            putString(ResultBottomSheet.ARG_TITLE, title)
        }
        return when (position) {
            0 -> HospitalFragment()
            else -> ArticleDiseaseFragment.newInstance(disease)
        }
    }

}