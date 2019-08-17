package com.example.flupic.util

import android.content.Context

fun convertDpToPx(context: Context, dp: Float): Float {
    return dp * context.getResources().getDisplayMetrics().density
}

fun convertPxToDp(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.density
}