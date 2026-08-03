package com.mas.quranwords.ui.common

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.abs

open class OnSwipeTouchListener(context: Context) : View.OnTouchListener {

    companion object {
        private const val SWIPE_DISTANCE_THRESHOLD_DP = 100
    }

    private val viewConfiguration = ViewConfiguration.get(context)
    private val minFlingVelocity = viewConfiguration.scaledMinimumFlingVelocity
    private val density = context.resources.displayMetrics.density
    private val swipeDistanceThreshold = SWIPE_DISTANCE_THRESHOLD_DP * density

    private val gestureDetector = GestureDetector(context, GestureListener())

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        // Request parent view (like ScrollView/RecyclerView) not to steal swipe events
        if (event.action == MotionEvent.ACTION_DOWN) {
            v?.parent?.requestDisallowInterceptTouchEvent(true)
        }
        return gestureDetector.onTouchEvent(event)
    }

    open fun onSwipeLeft() {}
    open fun onSwipeRight() {}

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean = true

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 == null) return false

            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y

            if (abs(diffX) > abs(diffY)) {
                val isDistanceMet = abs(diffX) > swipeDistanceThreshold
                val isVelocityMet = abs(velocityX) > minFlingVelocity

                if (isDistanceMet || isVelocityMet) {
                    if (diffX > 0) {
                        onSwipeRight()
                    } else {
                        onSwipeLeft()
                    }
                    return true
                }
            }
            return false
        }
    }
}