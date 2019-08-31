package com.example.flupic.util

import android.content.Context
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.graphics.drawable.DrawableCompat
import android.os.Build
import androidx.core.content.ContextCompat
import android.graphics.drawable.Drawable



fun convertDpToPx(context: Context, dp: Float): Float {
    return dp * context.getResources().getDisplayMetrics().density
}

fun convertPxToDp(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.density
}

fun getBitmapFromVectorDrawable(context: Context, drawableId: Int, zoom: Float): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)

    val bitmap = Bitmap.createBitmap(
        (drawable!!.intrinsicWidth * zoom).toInt(),
        (drawable.intrinsicHeight * zoom).toInt(), Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}