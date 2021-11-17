package com.example.musiqal.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.musiqal.R
import com.example.musiqal.datamodels.youtubeItemInList.Item

class MusicPlayerViewPagerAdapter constructor(
    val layoutInflater: LayoutInflater,
    val audioItemList: List<Item>,
    val context: Context
) : PagerAdapter() {

    var glideImageLoader = LoadDrawableFromGLide()
    override fun getCount(): Int {
        return audioItemList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view =
            layoutInflater.inflate(R.layout.custome_view_pager_item_list, container, false)
        val songImageView = view.findViewById<ImageView>(R.id.custome_viewPager_imageView)
        val audioItem = audioItemList.get(position)
        if (audioItem.snippet.title.equals(Constants.PRIVATE_VIDEO))
        {
            Glide.with(context)
                .load(R.drawable.private_video)
                .into(songImageView)
        }
        else
        {
            val imageUrl = ImageUrlUtil.getMaxResolutionImageUrl(item = audioItem)
            glideImageLoader.loadImage(imageUrl, context, songImageView)
        }

        container.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView((`object` as View))
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }


}