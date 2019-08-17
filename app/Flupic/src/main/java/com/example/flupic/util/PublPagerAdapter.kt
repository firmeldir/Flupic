package com.example.flupic.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.flupic.ui.inside.ownPublFragment
import com.example.flupic.ui.inside.savedPublFragment

class PublPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ownPublFragment()
            }
            else -> {
                return savedPublFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Publications"
            else -> {
                return "Saved"
            }
        }
    }
}