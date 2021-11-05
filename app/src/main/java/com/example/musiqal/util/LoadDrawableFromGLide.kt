package com.example.musiqal.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class LoadDrawableFromGLide {
    fun loadImage(imgUrl: String, context: Context, view: View) {
        Glide.with(context)
            .load(imgUrl)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    view.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }
}