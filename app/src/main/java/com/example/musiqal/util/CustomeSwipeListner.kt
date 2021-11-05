package com.example.customegesturesdecoder

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class CustomeSwiperListner(val view: View, val onViewSwipeListener: OnViewSwipeListener) :
    View.OnTouchListener {
    lateinit var gestureDetector: GestureDetector



    init {

        val startPoint = 100;
        val startVolicity = 100;

        val gestureDetectorListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                val diffInXAix = (e2?.x!! - e1?.x!!)
                val diffInYAix = (e2.y - e1.y)
                try {
                    if (Math.abs(diffInXAix) > Math.abs(diffInYAix)) {
                        if (Math.abs(diffInXAix) > startPoint && Math.abs(velocityX) > startVolicity) {
                            if (diffInXAix > 0) {
                                onViewSwipeListener.onSwipeRight()
                            } else {
                                onViewSwipeListener.onSwipeLeft()
                            }
                        }
                        return true
                    } else {
                        if (Math.abs(diffInYAix) > startPoint && Math.abs(velocityY) > startVolicity) {
                            if (diffInXAix > 0) {

                                onViewSwipeListener.onSwipeDown()
                            } else {

                                onViewSwipeListener.onSwipeUp()
                            }

                        }
                        return true
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return false


            }

        }
        gestureDetector = GestureDetector(gestureDetectorListener)
        view.setOnTouchListener(this)


    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)

    }
}