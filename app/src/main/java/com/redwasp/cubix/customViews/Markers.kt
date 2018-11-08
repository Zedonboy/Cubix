package com.redwasp.cubix.customViews

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import com.redwasp.cubix.utils.Utils

class Markers(cntx : Context) : View(cntx) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#FAFAFB")
        strokeWidth = 16f
    }
    private var _radius = 32
    private val path = Path()
    var radius = 0
    set(value) {
        _radius = value
        invalidate()
    }

    private val mAnimator = ValueAnimator.ofFloat(1f, 1.5f).apply {
        repeatCount = ValueAnimator.INFINITE
        duration = 300
        addUpdateListener {
            paint.color = Utils.lightenColor(Color.parseColor("#45A163"), animatedValue as Float)
            invalidate()
        }
    }
    private var Cx = 0f ; private var Cy = 0f
    private var C2x = 0f; private var C2y = 0f
    private var C3x = 0f; private var C3y = 0f
    private var C4x = 0f; private var C4y = 0f

    init {
        mAnimator.start()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawCircle(Cx,Cy,_radius.toFloat(), paint)
        canvas?.drawCircle(C2x, C2y, _radius.toFloat(), paint)
        canvas?.drawCircle(C3x, C3y, _radius.toFloat(), paint)
        canvas?.drawCircle(C4x, C4y, _radius.toFloat(), paint)

        path.moveTo(Cx, Cy)
        path.lineTo(C2x, C2y)
        path.lineTo(C3x, C3y)
        path.lineTo(C4x, C4y)
        path.lineTo(Cx, Cy)
        path.close()

        canvas?.drawPath(path, linePaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?:return false
        val action = event.actionMasked
        return when(action){
            MotionEvent.ACTION_MOVE -> {
                if (mAnimator.isRunning) mAnimator.end()
                Cx = event.x
                Cy = event.y
                true
            }

            MotionEvent.ACTION_DOWN -> {
                if (mAnimator.isRunning) mAnimator.end()
                true
            }

            MotionEvent.ACTION_UP -> {
                mAnimator.start()
                true
            }
            else -> false
        }
    }
}