package com.example.movies.ui.main

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.model.GENRES
import com.example.movies.net.POSTER_URL

const val POSTER_WIDTH_92 = "/w92"
const val POSTER_WIDTH_500 = "/w500"

@BindingAdapter("poster")
internal fun ImageView.setPoster(path: String?) {
    path.takeUnless { it.isNullOrEmpty() }?.also { it ->
        Glide
            .with(context)
            .load("$POSTER_URL$POSTER_WIDTH_92$it")
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher)
            .into(this)
    }
}

@BindingAdapter("backdrop")
internal fun ImageView.setBackdrop(path: String?) {
    path.takeUnless { it.isNullOrEmpty() }?.also { it ->
        Glide
            .with(context)
            .load("$POSTER_URL$POSTER_WIDTH_500$it")
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher)
            .into(this)
    }
}

@BindingAdapter("genres")
internal fun LinearLayoutCompat.setGenres(list: List<Int>?) {
    list?.takeIf { it.isNotEmpty() }?.also { it ->
        it.forEach { genre ->
            val tv = TextView(context)
            with(tv) {
                text = GENRES[genre]
                background = null
                gravity = Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                setBackgroundResource(R.drawable.border)

                setPadding(8.dpToPx(context), 0, 8.dpToPx(context), 0)
                layoutParams = LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                ).also {
                    it.setMargins(0, 0, 8.dpToPx(context), 0)
                }
            }
            addView(tv)
        }
        invalidate()
    }
}

private fun Int.dpToPx(context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}