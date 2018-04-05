package com.qwert2603.andrlib.util

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import android.util.TypedValue

fun Resources.color(@ColorRes colorRes: Int) = ResourcesCompat.getColor(this, colorRes, null)
fun Resources.drawable(@DrawableRes drawableRes: Int): Drawable = ResourcesCompat.getDrawable(this, drawableRes, null)!!
fun Resources.toPx(dp: Int): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics).toInt()