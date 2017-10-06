package adrygraph.simplelauncher.settings.ui

import adrygraph.simplelauncher.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

@SuppressLint("ViewConstructor")
/**
 * Created by Audrey on 24/09/2017.
 * Color picker View
 */
class ColorPickerView internal constructor(c: Context, private val mListener: ColorPickerDialog.OnColorChangedListener, private var mCurrentColor: Int, private val mDefaultColor: Int) : View(c) {
    private val mPaint: Paint
    private var mCurrentHue = 0f
    private var mCurrentX = 0
    private var mCurrentY = 0
   private var widthDialog: Float
    private var heightDialog: Float
    private var mGradientHeight: Float
    private var mLineColorHeight: Float
    private var mMargin: Float = resources.getDimensionPixelSize(R.dimen.color_picker_margin).toFloat()
    private var mStepWidth : Float
    private var buttonHeight : Float
    private var mDensity : Float
    private var mButtonPickText = resources.getString(R.string.color_picker_pick)
    private var mButtonCurrentText = resources.getString(R.string.color_picker_current)
    private var mColorDivider: Int
    private var mMainColors : IntArray
    private var mHueBarColors : IntArray
    private val mGradientColors = IntArray(2)
    private val mColorBox = RectF(0f, 0f, 0f, 0f)
    private var mPaintShader : LinearGradient



    // Get the current selected color from the hue bar
    private val currentMainColor: Int
        get() {
            val translatedHue = 255 - (mCurrentHue * 255 / 360).toInt()
            var index = 0
            run {
                var i = 0f
                while (i < 256) {
                    if (index == translatedHue)
                        return Color.rgb(255, 0, i.toInt())
                    index++
                    i += (256 / mColorDivider).toFloat()
                }
            }
            run {
                var i = 0f
                while (i < 256) {
                    if (index == translatedHue)
                        return Color.rgb(255 - i.toInt(), 0, 255)
                    index++
                    i += (256 / mColorDivider).toFloat()
                }
            }
            run {
                var i = 0f
                while (i < 256) {
                    if (index == translatedHue)
                        return Color.rgb(0, i.toInt(), 255)
                    index++
                    i += (256 / mColorDivider).toFloat()
                }
            }
            run {
                var i = 0f
                while (i < 256) {
                    if (index == translatedHue)
                        return Color.rgb(0, 255, 255 - i.toInt())
                    index++
                    i += (256 / mColorDivider).toFloat()
                }
            }
            run {
                var i = 0f
                while (i < 256) {
                    if (index == translatedHue)
                        return Color.rgb(i.toInt(), 255, 0)
                    index++
                    i += (256 / mColorDivider).toFloat()
                }
            }
            var i = 0f
            while (i < 256) {
                if (index == translatedHue)
                    return Color.rgb(255, 255 - i.toInt(), 0)
                index++
                i += (256 / mColorDivider).toFloat()
            }
            return Color.RED
        }



    init {

        widthDialog = resources.displayMetrics.widthPixels.toFloat() - (mMargin * 4f)
        heightDialog = widthDialog * 4F /5F


        mGradientHeight = resources.getDimensionPixelSize(R.dimen.color_picker_gradient_height).toFloat()
        mLineColorHeight = resources.getDimensionPixelSize(R.dimen.color_picker_line_color_height).toFloat()
        mPaintShader = LinearGradient(0F, 0F, 0F, mGradientHeight, mGradientColors, null, Shader.TileMode.MIRROR)

        mStepWidth = (widthDialog) / 255f
        buttonHeight = resources.getDimensionPixelSize(R.dimen.color_picker_buttons_height).toFloat()
        mDensity = resources.displayMetrics.density

        mColorDivider = 42
        mMainColors = IntArray(65536)

        mHueBarColors = IntArray(258)

        // Get the current hue from the current color and update the main
        // color field
        val hsv = FloatArray(3)
        Color.colorToHSV(mCurrentColor, hsv)
        mCurrentHue = hsv[0]
        updateMainColors()

        // Initialize the colors of the hue slider bar
        var index = 0
        run {
            var i = 0f
            while (i < 256)
            // Red (#f00) to pink
            // (#f0f)
            {
                mHueBarColors[index] = Color.rgb(255, 0, i.toInt())
                index++
                i += (256 / mColorDivider).toFloat()
            }
        }
        run {
            var i = 0f
            while (i < 256)
            // Pink (#f0f) to blue
            // (#00f)
            {
                mHueBarColors[index] = Color.rgb(255 - i.toInt(), 0, 255)
                index++
                i += (256 / mColorDivider).toFloat()
            }
        }
        run {
            var i = 0f
            while (i < 256)
            // Blue (#00f) to light
            // blue (#0ff)
            {
                mHueBarColors[index] = Color.rgb(0, i.toInt(), 255)
                index++
                i += (256 / mColorDivider).toFloat()
            }
        }
        run {
            var i = 0f
            while (i < 256)
            // Light blue (#0ff) to
            // green (#0f0)
            {
                mHueBarColors[index] = Color.rgb(0, 255, 255 - i.toInt())
                index++
                i += (256 / mColorDivider).toFloat()
            }
        }
        run {
            var i = 0f
            while (i < 256)
            // Green (#0f0) to yellow
            // (#ff0)
            {
                mHueBarColors[index] = Color.rgb(i.toInt(), 255, 0)
                index++
                i += (256 / mColorDivider).toFloat()
            }
        }
        var i = 0f
        while (i < 256)
        // Yellow (#ff0) to red
        // (#f00)
        {
            mHueBarColors[index] = Color.rgb(255, 255 - i.toInt(), 0)
            index++
            i += (256 / mColorDivider).toFloat()
        }

        // Initializes the Paint that will draw the View
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.textAlign = Paint.Align.CENTER
        val fontSize = resources.getDimensionPixelSize(R.dimen.button_font_size)
        mPaint.textSize = fontSize.toFloat()
    }

    // Update the main field colors depending on the current selected hue
    private fun updateMainColors() {
        val mainColor = currentMainColor
        var index = 0
        val topColors = IntArray(256)
        for (y in 0..255) {
            for (x in 0..255) {
                if (y == 0) {
                    mMainColors[index] = Color.rgb(
                            255 - (255 - Color.red(mainColor)) * x / 255,
                            255 - (255 - Color.green(mainColor)) * x / 255,
                            255 - (255 - Color.blue(mainColor)) * x / 255)
                    topColors[x] = mMainColors[index]
                } else
                    mMainColors[index] = Color.rgb(
                            (255 - y) * Color.red(topColors[x]) / 255,
                            (255 - y) * Color.green(topColors[x]) / 255,
                            (255 - y) * Color.blue(topColors[x]) / 255)
                index++
            }
        }
    }



    @SuppressLint("DrawAllocation") //TODO
    override fun onDraw(canvas: Canvas) {
        val translatedHue = 255 - (mCurrentHue * 255 / 360).toInt()
        for (x in 0..255) {
                if (translatedHue != x) {
                    mPaint.color = mHueBarColors[x]
                    mPaint.strokeWidth = 0F
                } else
                {
                    mPaint.color = Color.BLACK
                    mPaint.strokeWidth = mStepWidth
                }
                mPaint.style = Paint.Style.FILL
                mColorBox.left = x.toFloat() * mStepWidth
                mColorBox.top = 0f
                mColorBox.right = x.toFloat() * mStepWidth + mStepWidth
                mColorBox.bottom = mLineColorHeight
                canvas.drawRect(mColorBox, mPaint)
        }

        for (x in 0..255) {
            mGradientColors[0] = mMainColors[x]
            mGradientColors[1] = Color.BLACK
            mPaintShader = LinearGradient(0F, 0F, 0F, mGradientHeight, mGradientColors, null, Shader.TileMode.MIRROR)
            mPaint.shader = mPaintShader
            mPaint.style = Paint.Style.FILL
            mColorBox.left = x.toFloat() * mStepWidth
            mColorBox.top = mLineColorHeight
            mColorBox.right = x.toFloat() * mStepWidth + mStepWidth
            mColorBox.bottom = mGradientHeight
            canvas.drawRect(mColorBox, mPaint)
        }
        mPaint.shader = null

        // Display the circle around the currently selected color in the
        // main field
        if (mCurrentX != 0 && mCurrentY != 0) {
            mPaint.style = Paint.Style.STROKE
            mPaint.color = Color.BLACK
            canvas.drawCircle(mCurrentX.toFloat(), mCurrentY.toFloat(), mMargin, mPaint)
        }

        // Draw a 'button' with the currently selected color
        mPaint.style = Paint.Style.FILL
        mPaint.color = mCurrentColor
        canvas.drawRect(0F, heightDialog - buttonHeight, widthDialog / 2,  heightDialog, mPaint)

        // Set the text color according to the brightness of the color
        if (Color.red(mCurrentColor) + Color.green(mCurrentColor) + Color.blue(mCurrentColor) < 384)
            mPaint.color = Color.WHITE
        else
            mPaint.color = Color.BLACK
        canvas.drawText(mButtonPickText, widthDialog / 4f, heightDialog - buttonHeight / 3, mPaint)

        // Draw a 'button' with the default color
        mPaint.style = Paint.Style.FILL
        mPaint.color = mDefaultColor
        canvas.drawRect(widthDialog / 2, heightDialog - buttonHeight, widthDialog, heightDialog, mPaint)

        // Set the text color according to the brightness of the color
        if ((Color.red(mDefaultColor) + Color.green(mDefaultColor)
                + Color.blue(mDefaultColor)) < 384)
            mPaint.color = Color.WHITE
        else
            mPaint.color = Color.BLACK
        canvas.drawText(mButtonCurrentText, widthDialog - widthDialog / 4F, heightDialog - buttonHeight / 3,
                mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthDialog.toInt(), heightDialog.toInt())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN)
            return true
        val x = event.x
        val y = event.y

        // If the touch event is located in the hue bar
        if (x > 0 && x < widthDialog && y > 0 && y <= mLineColorHeight) {
            // Update the main field colors
            mCurrentHue = (255 - x / mStepWidth) * 360 / 255
            updateMainColors()

            // Update the current selected color
            val transX = mCurrentX
            val transY = mCurrentY
            val index = 256 * (transY - 1) + transX
            if (index > 0 && index < mMainColors.size)
                mCurrentColor = mMainColors[(256 * (transY - 1) + transX)]

            // Force the redraw of the dialog
            invalidate()
            return true
        }

        // If the touch event is located in the main field
        if (x > 0 && x < widthDialog && y > mLineColorHeight && y < heightDialog - buttonHeight) {
            mCurrentX = x.toInt()
            mCurrentY = y.toInt()
            val transX = mCurrentX  / mStepWidth
            val transY = (mCurrentY - mLineColorHeight) / mDensity
            val index = 256 * (transY - 1) + transX
            if (index > 0 && index < mMainColors.size) {
                // Update the current color
                mCurrentColor = mMainColors[index.toInt()]
                // Force the redraw of the dialog
                invalidate()
            }
            return true
        }

        // If the touch event is located in the left button, notify the
        // listener with the current color
        if (x > 0 && x < widthDialog / 2 && y > heightDialog - buttonHeight && y < heightDialog)
            mListener.colorChanged(mCurrentColor)

        // If the touch event is located in the right button, notify the
        // listener with the default color
        if (x > widthDialog / 2 && x < widthDialog && y > heightDialog - buttonHeight && y < heightDialog)
            mListener.colorChanged(mDefaultColor)

        return true
    }
}