package com.android.marvel.common.infrastructure

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class ResourcesAccessor (private val context : Context) {

    fun getString(resId: Int): String {
        return context.getString(resId)
    }

    fun getString(resId: Int, vararg args: String): String {
        return context.getString(resId, args)
    }

    fun getArray(resId: Int): Array<String> {
        return context.resources.getStringArray(resId)
    }

    fun getColor(colorResId: Int): Int {
        return ContextCompat.getColor(context, colorResId)
    }

    fun getDrawable(drawableResId: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawableResId)
    }
}