package adrygraph.simplelauncher

import adrygraph.simplelauncher.settings.ui.ColorPickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup




class MainActivity : AppCompatActivity(), ColorPickerDialog.OnColorChangedListener {

    private var mRootView : ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRootView = findViewById(R.id.activityMainRootViewCL)!!
        setBackground(AppData.getBackgroundColorInPref())
    }

    override fun onBackPressed() {
        displayColorPicker()
    }

    private fun displayColorPicker() {
        ColorPickerDialog(this@MainActivity, this@MainActivity, AppData.getBackgroundColorInPref()).show()
    }

    override fun colorChanged(color: Int) {
        AppData.writeBackgroundColorInPref(color)
        setBackground(color)
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(SimpleLauncherApp.BROADCAST_BG_COLOR_CHANGED))
    }

    private fun setBackground(color : Int) {
        mRootView!!.setBackgroundColor(color)
    }
}
