package adrygraph.simplelauncher.settings.ui

import adrygraph.simplelauncher.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup

class SettingsActivity : AppCompatActivity(), ColorPickerDialog.OnColorChangedListener  {

    private var mRootView : ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        mRootView = findViewById(R.id.activitySettingsRootViewCL)!!
    }

    override fun colorChanged(color: Int) {
        val data = Intent()
        data.putExtra("COLOR", color)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}