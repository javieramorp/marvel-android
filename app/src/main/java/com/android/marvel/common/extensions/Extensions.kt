package com.android.marvel.common.extensions

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.android.marvel.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest


inline fun <T> Flow<T>.observe(owner: LifecycleOwner, crossinline collector: (T) -> Unit) {
    owner.lifecycleScope.launch {
        owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@observe.collect {
                Log.e("TAG: $owner", "Event: $it")
                collector(it)
            }
        }
    }
}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
}

fun ImageView.loadUrl(url: String?, placeHolder: Int = R.drawable.logo_circle_marvel) {
    Glide.with(context).load(url).placeholder(placeHolder).into(this)
}
