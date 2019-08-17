package com.example.flupic.util

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.viewpager.widget.ViewPager

class AddPageListener(private val animator: MotionLayout) :  ViewPager.OnPageChangeListener {


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int){
        val newProgress = (position + positionOffset) / 2
        animator.progress = newProgress
    }


    override fun onPageScrollStateChanged(state: Int) = Unit
    override fun onPageSelected(position: Int) = Unit
}