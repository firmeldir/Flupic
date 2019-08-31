package com.example.flupic.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.flupic.ui.inside.ownPublFragment
import com.example.flupic.ui.inside.savedPublFragment

class PublPagerAdapter(fm: FragmentManager, private val profileUID:String = "0", private val saved:savedPublFragment = savedPublFragment()) : FragmentPagerAdapter(fm) {

    private val own:ownPublFragment = if(profileUID == "0"){
        ownPublFragment()
    }else{
        ownPublFragment(profileUID)
    }

    override fun getItem(position: Int): Fragment = when (position) {
            0 -> own
            1 -> saved
            else -> own
            }

    override fun getCount(): Int {
        return if(profileUID == "0"){
            2
        }else{
            1
        }
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