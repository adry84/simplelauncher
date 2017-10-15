package adrygraph.simplelauncher.settings.ui

import adrygraph.simplelauncher.SimpleLauncherApp
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View


/**
 * Created by Audrey on 15/10/2017.
 * Wheel Color Picker View
 */
class ColorPickerView2 internal constructor(c: Context, private val mListener: ColorPickerDialog.OnColorChangedListener, color: Int) : View(c) {
    private val mPaint: Paint
    private val mCenterPaint: Paint
    private val mColors: IntArray = intArrayOf(-0x10000, -0xff01, -0xffff01, -0xff0001, -0xff0100, -0x100, -0x10000)
    private var mTrackingCenter: Boolean = false
    private var mHighlightCenter: Boolean = false

    init {
        val s = SweepGradient(0f, 0f, mColors, null)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.shader = s
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = CENTER_RADIUS
        mCenterPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCenterPaint.color = color
        mCenterPaint.strokeWidth = 5F
    }

    val rectF = RectF()

    override fun onDraw(canvas: Canvas) {
        val r = CENTER_X - mPaint.strokeWidth * 0.5f
        canvas.translate(CENTER_X, CENTER_X)
        rectF.left = -r
        rectF.top = -r
        rectF.right = r
        rectF.bottom = r
        canvas.drawOval(rectF, mPaint)
        canvas.drawCircle(0F, 0F, CENTER_RADIUS, mCenterPaint)
        if (mTrackingCenter) {
            val c = mCenterPaint.color
            mCenterPaint.style = Paint.Style.STROKE
            if (mHighlightCenter) {
                mCenterPaint.alpha = 0xFF
            } else {
                mCenterPaint.alpha = 0x80
            }
            canvas.drawCircle(0F, 0F,
                    CENTER_RADIUS + mCenterPaint.strokeWidth,
                    mCenterPaint)
            mCenterPaint.style = Paint.Style.FILL
            mCenterPaint.color = c
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension((CENTER_X * 2).toInt(), (CENTER_Y * 2).toInt())
    }

    private fun floatToByte(x: Float): Int {
        return Math.round(x)
    }

    private fun pinToByte(n: Int): Int {
        var color = n
        if (color < 0) {
            color = 0
        } else if (color > 255) {
            color = 255
        }
        return color
    }

    private fun ave(s: Int, d: Int, p: Float): Int {
        return s + java.lang.Math.round(p * (d - s))
    }

    private fun interpColor(colors: IntArray, unit: Float): Int {
        if (unit <= 0) {
            return colors[0]
        }
        if (unit >= 1) {
            return colors[colors.size - 1]
        }
        var p = unit * (colors.size - 1)
        val i = p.toInt()
        p -= i.toFloat()
        // now p is just the fractional part [0...1) and i is the index
        val c0 = colors[i]
        val c1 = colors[i + 1]
        val a = ave(Color.alpha(c0), Color.alpha(c1), p)
        val r = ave(Color.red(c0), Color.red(c1), p)
        val g = ave(Color.green(c0), Color.green(c1), p)
        val b = ave(Color.blue(c0), Color.blue(c1), p)
        return Color.argb(a, r, g, b)
    }

    @Suppress("unused")
    private fun rotateColor(color: Int, rad: Float): Int {
        val deg = rad * 180 / 3.1415927f
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        val cm = ColorMatrix()
        val tmp = ColorMatrix()
        cm.setRGB2YUV()
        tmp.setRotate(0, deg)
        cm.postConcat(tmp)
        tmp.setYUV2RGB()
        cm.postConcat(tmp)
        val a = cm.array
        val ir = floatToByte(a[0] * r + a[1] * g + a[2] * b)
        val ig = floatToByte(a[5] * r + a[6] * g + a[7] * b)
        val ib = floatToByte(a[10] * r + a[11] * g + a[12] * b)
        return Color.argb(Color.alpha(color), pinToByte(ir),
                pinToByte(ig), pinToByte(ib))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x - CENTER_X
        val y = event.y - CENTER_Y
        val inCenter = java.lang.Math.hypot(x.toDouble(), y.toDouble()) <= CENTER_RADIUS
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mTrackingCenter = inCenter
                if (inCenter) {
                    mHighlightCenter = true
                    invalidate()
                    return true
                }
                if (mTrackingCenter) {
                    if (mHighlightCenter != inCenter) {
                        mHighlightCenter = inCenter
                        invalidate()
                    }
                } else {
                    val angle = java.lang.Math.atan2(y.toDouble(), x.toDouble()).toFloat()
                    // need to turn angle [-PI ... PI] into unit [0....1]
                    var unit = angle / (2 * PI)
                    if (unit < 0) {
                        unit += 1f
                    }
                    mCenterPaint.color = interpColor(mColors, unit)
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> if (mTrackingCenter) {
                if (mHighlightCenter != inCenter) {
                    mHighlightCenter = inCenter
                    invalidate()
                }
            } else {
                val angle = java.lang.Math.atan2(y.toDouble(), x.toDouble()).toFloat()
                var unit = angle / (2 * PI)
                if (unit < 0) {
                    unit += 1f
                }
                mCenterPaint.color = interpColor(mColors, unit)
                invalidate()
            }
            MotionEvent.ACTION_UP -> if (mTrackingCenter) {
                if (inCenter) {
                    mListener.colorChanged(mCenterPaint.color)
                }
                mTrackingCenter = false    // so we draw w/o halo
                invalidate()
            }
        }
        return true
    }

    companion object {
        private val screenScale = SimpleLauncherApp.instance.getResources().getDisplayMetrics().density * 1.8f
        private val CENTER_X = 100F * screenScale
        private val CENTER_Y = 100F * screenScale
        private val CENTER_RADIUS = 32F * screenScale
        private val PI = 3.1415926f
    }
}