package adrygraph.simplelauncher.settings.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle





/**
 * Created by Audrey on 24/09/2017.
 * Color picker dialog
 */
class ColorPickerDialog(context: Context, private var mListener: OnColorChangedListener,  private val mInitialColor: Int) : Dialog(context) {

    interface OnColorChangedListener {
        fun colorChanged(color: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val l = object : OnColorChangedListener {
            override fun colorChanged(color: Int) {
                mListener.colorChanged(color)
                dismiss()
            }
        }

        setContentView(ColorPickerView(context, l, mInitialColor))
    }
}