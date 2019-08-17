package com.example.flupic.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.flupic.ui.addTools.ingredientsFragment
import com.example.flupic.ui.addTools.mainInfoAdd
import com.example.flupic.ui.addTools.recipeAdd
import com.example.flupic.ui.inside.savedPublFragment

class AddPecipeAdapter(fm: FragmentManager,
                        val fragment: ingredientsFragment = ingredientsFragment(),
                        val fragment2: mainInfoAdd = mainInfoAdd(),
                        val fragment3: recipeAdd = recipeAdd()) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> { fragment2 }
            1 -> { fragment }
            else -> { fragment3 }
        }
    }

    override fun getCount(): Int {
        return 3
    }
}