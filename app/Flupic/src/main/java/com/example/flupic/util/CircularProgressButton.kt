package com.example.flupic.util

import android.animation.*
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import android.view.animation.AnimationSet
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.example.flupic.R


class LoadingButton(private val constext: Context, attr: AttributeSet? = null) : Button(constext, attr){

    enum class State{
        IDLE, LOAD
    }


    private lateinit var gradientDrawable: GradientDrawable
    private var curState = State.IDLE
    private val animSet = AnimatorSet()
    private var isMorphingInProgress = false

    init {
        (ContextCompat.getDrawable(context, R.drawable.button_shape_default) as? GradientDrawable)?.let {
            background = it
            gradientDrawable = it
        }
    }

    fun startAnimation() {

        if (curState != State.IDLE) {
            return
        }else{
            curState = State.LOAD
        }

        val initialCornerRadius = 0
        val finalCornerRadius = 300
        val toWidth = 300

        isMorphingInProgress = true ; this.text = null ; isClickable = false


        val cornerAnimation = ValueAnimator.ofFloat(initialCornerRadius.toFloat(), finalCornerRadius.toFloat())
        cornerAnimation.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Float
            gradientDrawable.cornerRadius = `val`
        }

//            ObjectAnimator.ofFloat(
//            gradientDrawable,
//            "cornerRadius",
//            initialCornerRadius.toFloat(),
//            finalCornerRadius.toFloat()
//        )

        val widthAnimation = ValueAnimator.ofInt(width, toWidth)
        widthAnimation.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = layoutParams
            layoutParams.width = `val`
            setLayoutParams(layoutParams)
        }

        val heightAnimation = ValueAnimator.ofInt(height, toWidth)
        heightAnimation.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = layoutParams
            layoutParams.height = `val`
            setLayoutParams(layoutParams)
        }


        with(animSet){
            removeAllListeners()
            end()
            cancel()
            duration = 300
            playTogether(widthAnimation, heightAnimation)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    isMorphingInProgress = false
                }
            })
            start()
        }
    }
}



