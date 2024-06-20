package com.bangkit.lungxcan.ui.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.lungxcan.ui.result.articledisease.ArticleDiseaseFragment
import com.bangkit.lungxcan.ui.result.hospital.HospitalFragment


class PagerAdapter(fragment: FragmentActivity, private val disease: String) :
    FragmentStateAdapter(fragment) {

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