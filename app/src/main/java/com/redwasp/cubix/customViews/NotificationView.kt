package com.redwasp.cubix.customViews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.annotation.ColorInt
import android.support.annotation.Size
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.redwasp.cubix.R
import com.redwasp.cubix.utils.Utils
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class NotificationView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val path = Path()
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        setShadowLayer(5f,5f,5f, Color.GRAY)
    }
    private val textPaint = TextPaint().apply {
        textSize = 21f
        typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
    }
    private val tipPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private var onCloseListener : ((View) -> Unit) = fun(view : View){ view.visibility = View.GONE}
    private var mDetector : GestureDetector
    private var tipoffset = 0
    private var tipheight = 0
    private var tipgravity = 0
    private var tipBaseWidth = 0
    private var cornerRadius = 0
    private lateinit var staticLayout : StaticLayout
    private lateinit var bmp : Bitmap
    private lateinit var rect : RectF
    private var _notif_text = ""
    private var lastColor = 0
    var text
    get() = _notif_text
    set(value) {
        _notif_text = value
        invalidate()
    }

    init {
        val typedArrays = context.theme.obtainStyledAttributes(attributeSet, R.styleable.NotificationView, 0, 0)
        tipoffset = typedArrays.getDimensionPixelSize(R.styleable.NotificationView_tip_offset, measuredWidth/2)
        tipgravity = typedArrays.getInteger(R.styleable.NotificationView_tip_gravity, 0)
        tipheight = typedArrays.getDimensionPixelSize(R.styleable.NotificationView_tip_height, 10)
        tipBaseWidth = typedArrays.getDimensionPixelSize(R.styleable.NotificationView_tip_base_width, 10)
        cornerRadius = typedArrays.getDimensionPixelSize(R.styleable.NotificationView_corner_radius, 0)
        _notif_text = typedArrays.getString(R.styleable.NotificationView_notif_text)?:""
        val fillColor = typedArrays.getColor(R.styleable.NotificationView_fill_color, Color.BLUE)
        val textColor = typedArrays.getColor(R.styleable.NotificationView_text_color, Color.WHITE)
        textPaint.color = textColor
        fillPaint.color = fillColor
        tipPaint.color = fillColor
        lastColor = fillColor
        // must be initialized first before you can use coords
        mDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener(){
            override fun onDown(e: MotionEvent?): Boolean {
                // magic happens here
                // check whether the tap is within range the X bitmap range
                val x = e?.x?.toInt()!!
                val y = e.y.toInt()
                // bmpStartYaxis calculates the starting of the main body

                if (y in (tipheight + paddingTop)..(tipheight + paddingTop + 50) && x in (measuredWidth - paddingRight - 50)..(measuredWidth - paddingRight)){
                    onCloseListener.invoke(this@NotificationView)
                } else if(y in tipheight..(measuredHeight - paddingBottom) && x in paddingLeft..(measuredWidth - paddingRight)){
                    fillPaint.color = Utils.lightenColor(fillColor, 0.8f)
                    performClick()
                    invalidate()
                }
                return true
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                launch {
                    delay(300L, TimeUnit.MILLISECONDS)
                    withContext(UI){
                        fillPaint.color = lastColor
                        invalidate()
                    }
                }
                return true
            }
        })

        typedArrays.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        path.moveTo(tipoffset.toFloat(),tipheight.toFloat())
        path.lineTo(tipBaseWidth.toFloat()/2, 0f)
        path.lineTo(tipBaseWidth.toFloat(), tipheight.toFloat())
        canvas?.apply {
            drawRoundRect(rect, cornerRadius.toFloat(), cornerRadius.toFloat(),fillPaint)
            drawPath(path, tipPaint)
            val right = measuredWidth - paddingRight - 35
            canvas.drawBitmap(bmp,right.toFloat(), tipheight.toFloat() + 5, null)
            save()
            translate(10f, tipheight.toFloat()+20f)
            staticLayout.draw(this)
            restore()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        initializePaths()
    }

    private fun initializePaths(){
        rect = RectF(paddingLeft.toFloat(), tipheight.toFloat(), measuredWidth.toFloat() - paddingRight, measuredHeight.toFloat()-20)
        setLayerType(LAYER_TYPE_SOFTWARE, fillPaint)
        staticLayout = StaticLayout(_notif_text, textPaint, rect.width().toInt() - 20, Layout.Alignment.ALIGN_CENTER, 1f,1f, true)
        bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_close_white_24dp)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return mDetector.onTouchEvent(event)
    }

    fun setOnCloseListener(mListener : ((view : View) -> Unit)){
        onCloseListener = mListener
    }
}