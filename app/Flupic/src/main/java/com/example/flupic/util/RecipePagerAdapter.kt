package com.example.flupic.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.flupic.ui.inside.recipe.ingredientDetailFragment
import com.example.flupic.ui.inside.recipe.recipeDetailFragment

class RecipePagerAdapter(fm: FragmentManager,
                         val fragment: ingredientDetailFragment = ingredientDetailFragment(),
                         val fragment2: recipeDetailFragment = recipeDetailFragment()
                         ) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> { fragment }
            else -> { fragment2 }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Ingredients"
            else -> {
                return "Recipe"
            }
        }
    }
}