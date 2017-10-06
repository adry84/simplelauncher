package adrygraph.simplelauncher

import adrygraph.simplelauncher.settings.ui.ColorPickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup




class MainActivity : AppCompatActivity(), ColorPickerDialog.OnColorChangedListener {

    private var mRootView : ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRootView = findViewById(R.id.activityMainRootViewCL)!!
    }

    override fun onBackPressed() {
        displayColorPicker()
    }

    private fun displayColorPicker() {
        ColorPickerDialog(this@MainActivity, this@MainActivity)
                .show()
    }

    override fun colorChanged(color: Int) {
        setBackground(color)
    }

    private fun setBackground(color : Int) {
        mRootView!!.setBackgroundColor(color)
    }
}
