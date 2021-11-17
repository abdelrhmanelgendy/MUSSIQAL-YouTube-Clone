package com.example.musiqal.util

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.musiqal.R
import com.example.musiqal.datamodels.youtubeItemInList.Item
import com.example.musiqal.ui.slidingPan.SlidingUpDownPanel
import de.hdodenhof.circleimageview.CircleImageView

class ViewsColorEvaluter(
    val context: Context,
    val imageView: CircleImageView,
    val container: ConstraintLayout,
    val linearLayout: LinearLayout,
    val mainActiviytCoordinatorLayout: CoordinatorLayout,
   val mainActiviytSlidingLayout: SlidingUpDownPanel
) {
    var evaluatedColor=Color.BLACK

    fun generateColors(positionOffset: Float, item1: Item, item2: Item) {
        if (item1.snippet.title.equals(Constants.PRIVATE_VIDEO)) {
            generatePalleteWithoutFirstItem(item2, positionOffset)
        }
        if (item2.snippet.title.equals(Constants.PRIVATE_VIDEO)) {
            getneratePalleteWithoutSecondItem(item1, positionOffset)
        }
        if (!item1.snippet.title.equals(Constants.PRIVATE_VIDEO) && !item2.snippet.title.equals(
                Constants.PRIVATE_VIDEO
            )
        ) {
            generateNormalPallete(item1, item2, positionOffset)
        }
        if (item1.snippet.title.equals(Constants.PRIVATE_VIDEO) && !item2.snippet.title.equals(
                Constants.PRIVATE_VIDEO
            )
        ) {
            getneratePalleteWithoutFirstAndSecondItem(positionOffset)
        }
    }

    private fun getneratePalleteWithoutFirstAndSecondItem(
        positionOffset: Float
    ) {
        Glide.with(context)
            .asBitmap()
            .load(R.drawable.private_video)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    Palette.generateAsync(resource, object : Palette.PaletteAsyncListener {
                        override fun onGenerated(palette1: Palette?) {

                            Glide.with(context)
                                .asBitmap()
                                .load(R.drawable.private_video)
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        Palette.generateAsync(
                                            resource,
                                            object : Palette.PaletteAsyncListener {
                                                override fun onGenerated(palette2: Palette?) {
                                                    val colorEvaluator = getColorEvaluter(
                                                        Color.BLACK, Color.BLACK,
                                                        positionOffset
                                                    )
                                                    container.setBackgroundColor(
                                                        colorEvaluator
                                                    )
                                                    evaluatedColor=colorEvaluator
                                                    linearLayout.setBackgroundColor(colorEvaluator)
                                                    if (!mainActiviytSlidingLayout.isPanelExpanded)
                                                    {
                                                        mainActiviytCoordinatorLayout.setBackgroundColor(Color.BLACK)
                                                    }
                                                    else
                                                    {
                                                        mainActiviytCoordinatorLayout.setBackgroundColor(colorEvaluator)
                                                    }

                                                    imageView.setImageDrawable(
                                                        getDrawableFromColor(colorEvaluator)
                                                    )
                                                }
                                            })
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {

                                    }
                                })

                        }
                    })
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })

    }

    private fun generateNormalPallete(item1: Item, item2: Item, positionOffset: Float) {
        Glide.with(context)
            .asBitmap()
            .load(ImageUrlUtil.getMaxResolutionImageUrl(item1))
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    Palette.generateAsync(resource, object : Palette.PaletteAsyncListener {
                        override fun onGenerated(palette1: Palette?) {

                            Glide.with(context)
                                .asBitmap()
                                .load(ImageUrlUtil.getMaxResolutionImageUrl(item2))
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        Palette.generateAsync(
                                            resource,
                                            object : Palette.PaletteAsyncListener {
                                                override fun onGenerated(palette2: Palette?) {

                                                    val colorEvaluator = getColorEvaluter(
                                                        getPelleteColor(
                                                            palette1?.getDominantColor(
                                                                Color.BLACK
                                                            )!!
                                                        ),
                                                        getPelleteColor(
                                                            palette2?.getDominantColor(
                                                                Color.BLACK
                                                            )!!
                                                        ),
                                                        positionOffset
                                                    )


                                                    container.setBackgroundColor(
                                                        colorEvaluator
                                                    )
                                                    evaluatedColor=colorEvaluator
                                                    linearLayout.setBackgroundColor(colorEvaluator)
                                                    if (!mainActiviytSlidingLayout.isPanelExpanded)
                                                    {
                                                        mainActiviytCoordinatorLayout.setBackgroundColor(Color.BLACK)
                                                    }
                                                    else
                                                    {

                                                        mainActiviytCoordinatorLayout.setBackgroundColor(colorEvaluator)
                                                    }


                                                    imageView.setImageDrawable(
                                                        getDrawableFromColor(colorEvaluator)
                                                    )
                                                }
                                            })
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {

                                    }
                                })

                        }
                    })
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

    private fun getneratePalleteWithoutSecondItem(item1: Item, positionOffset: Float) {
        Glide.with(context)
            .asBitmap()
            .load(ImageUrlUtil.getMaxResolutionImageUrl(item1))
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    Palette.generateAsync(resource, object : Palette.PaletteAsyncListener {
                        override fun onGenerated(palette1: Palette?) {

                            Glide.with(context)
                                .asBitmap()
                                .load(R.drawable.private_video)
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        Palette.generateAsync(
                                            resource,
                                            object : Palette.PaletteAsyncListener {
                                                override fun onGenerated(palette2: Palette?) {
                                                    val colorEvaluator = getColorEvaluter(
                                                        getPelleteColor(
                                                            palette1?.getDominantColor(
                                                                Color.BLACK
                                                            )!!
                                                        ), Color.BLACK,
                                                        positionOffset
                                                    )
                                                    container.setBackgroundColor(
                                                        colorEvaluator
                                                    )
                                                    evaluatedColor=colorEvaluator
                                                    linearLayout.setBackgroundColor(colorEvaluator)
                                                    if (!mainActiviytSlidingLayout.isPanelExpanded)
                                                    {
                                                        mainActiviytCoordinatorLayout.setBackgroundColor(Color.BLACK)
                                                    }
                                                    else
                                                    {
                                                        mainActiviytCoordinatorLayout.setBackgroundColor(colorEvaluator)
                                                    }


                                                    imageView.setImageDrawable(
                                                        getDrawableFromColor(colorEvaluator)
                                                    )
                                                }
                                            })
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {

                                    }
                                })

                        }
                    })
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

    private fun generatePalleteWithoutFirstItem(item2: Item, positionOffset: Float) {
        Glide.with(context)
            .asBitmap()
            .load(R.drawable.private_video)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    Palette.generateAsync(resource, object : Palette.PaletteAsyncListener {
                        override fun onGenerated(palette1: Palette?) {

                            Glide.with(context)
                                .asBitmap()
                                .load(ImageUrlUtil.getMaxResolutionImageUrl(item2))
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        Palette.generateAsync(
                                            resource,
                                            object : Palette.PaletteAsyncListener {
                                                override fun onGenerated(palette2: Palette?) {
                                                    val colorEvaluator = getColorEvaluter(
                                                        Color.BLACK,
                                                        getPelleteColor(
                                                            palette2?.getDominantColor(
                                                                Color.BLACK
                                                            )!!
                                                        ),
                                                        positionOffset
                                                    )
                                                    container.setBackgroundColor(
                                                        colorEvaluator
                                                    )
                                                    evaluatedColor=colorEvaluator
                                                    linearLayout.setBackgroundColor(colorEvaluator)
                                                    if (!mainActiviytSlidingLayout.isPanelExpanded)
                                                    {
                                                        mainActiviytCoordinatorLayout.setBackgroundColor(Color.BLACK)
                                                    }
                                                    else
                                                    {
                                                        mainActiviytCoordinatorLayout.setBackgroundColor(colorEvaluator)
                                                    }


                                                    imageView.setImageDrawable(
                                                        getDrawableFromColor(colorEvaluator)
                                                    )
                                                }
                                            })
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {

                                    }
                                })

                        }
                    })
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

    private fun getDrawableFromColor(colorEvaluator: Int): Drawable {
        return ColorDrawable(colorEvaluator)
    }

    private fun getPelleteColor(dominantColor: Int): Int {
        return dominantColor

    }

    private fun getColorEvaluter(
        colorAtCurrentPosition: Int,
        colorAtSwitchedPosition: Int,
        positionOffset: Float
    ): Int {
        return ArgbEvaluator().evaluate(
            positionOffset,
            colorAtCurrentPosition,
            colorAtSwitchedPosition
        ) as Int

    }




}