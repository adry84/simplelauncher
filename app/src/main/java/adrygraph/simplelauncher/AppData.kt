package adrygraph.simplelauncher

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color


/**
 * Created by Audrey on 06/10/2017.
 * this class contains method to save and read app data
 */
class AppData {

    companion object {
        private val APP_PREFERENCES = "SLPREFS"
        private val PREF_COLOR_BG_LIST_APP = "COLOR_BG_LIST_APP"
        val DEFAULT_COLOR = Color.WHITE

        fun writeBackgroundColorInPref(color : Int) {
            val  sharePreferences : SharedPreferences = SimpleLauncherApp.instance.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            val editor = sharePreferences.edit()
            editor.putInt(PREF_COLOR_BG_LIST_APP, color)
            editor.apply()
        }

        fun getBackgroundColorInPref(): Int {
            val  sharePreferences : SharedPreferences = SimpleLauncherApp.instance.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            return sharePreferences.getInt(PREF_COLOR_BG_LIST_APP, DEFAULT_COLOR)
        }
    }
}