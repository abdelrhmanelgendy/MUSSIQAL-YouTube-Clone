package com.example.musiqal.bindingAdapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("setImageUrl")
fun ImageView.loadImageFromUrl(imgUrl: String) {
    this.load(imgUrl)
}

@BindingAdapter("setCircularImageUrl")
fun CircleImageView.loadImageFromUrl(imgUrl: String) {
    this.load(imgUrl)
}