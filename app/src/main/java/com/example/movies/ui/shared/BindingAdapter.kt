package com.example.movies.ui.shared

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.movies.R
import com.example.movies.model.GENRES
import com.example.movies.model.POSTER_WIDTH_500
import com.example.movies.model.POSTER_WIDTH_92
import com.example.movies.net.POSTER_URL
import com.google.android.flexbox.FlexboxLayout

@BindingAdapter("poster")
internal fun ImageView.setPoster(path: String?) {
    Glide
        .with(context)
        .load("$POSTER_URL$POSTER_WIDTH_92$path")
        .apply(getGlideOption())
        .into(this)
}

@BindingAdapter("backdrop")
internal fun ImageView.setBackdrop(path: String?) {
    Glide
        .with(context)
        .load("$POSTER_URL$POSTER_WIDTH_500$path")
        .apply(getGlideOption())
        .into(this)
}

@BindingAdapter("genres")
internal fun FlexboxLayout.setGenres(list: List<Int>?) {
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
                    it.setMargins(0, 8.dpToPx(context), 8.dpToPx(context), 0)
                }
            }
            addView(tv)
        }
        invalidate()
    }
}

private fun getGlideOption() = RequestOptions()
    .placeholder(R.mipmap.ic_launcher)
    .error(android.R.drawable.stat_notify_error)
    .fallback(ColorDrawable(Color.GRAY))
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .priority(Priority.HIGH)

private fun Int.dpToPx(context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}