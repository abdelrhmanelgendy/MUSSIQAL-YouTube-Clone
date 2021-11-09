package com.example.musiqal.util

import android.graphics.Bitmap
import com.bumptech.glide.Glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target


class GlideLoader {
    fun loadImageBitmap(view: ImageView, url: String?, listener: CustomTarget<Bitmap>) {
        Glide.with(view.getContext())
            .asBitmap()
            .load(url)
            .into(listener)
    }
    fun loadImageDrawable(view: ImageView, url: String?, listener: CustomTarget<Drawable>) {
        Glide.with(view.getContext())
            .asDrawable()
            .load(url)
            .into(listener)
    }

    fun loadImage(view: ImageView, url: String?) {
        Glide.with(view.getContext())
            .asDrawable()
            .load(url)
            .into(view)
    }

    fun loadImage(view: ImageView, id: Int?) {
        Glide.with(view.getContext())
            .asDrawable()
            .load(id)
            .into(view)
    }
}