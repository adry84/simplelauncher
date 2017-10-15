package adrygraph.simplelauncher

import adrygraph.simplelauncher.apps.models.AppModel
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color


/**
 * Created by Audrey on 06/10/2017.
 * this class contains method to save and read app data
 */
object AppData {

    private val APP_PREFERENCES = "SLPREFS"
    private val PREF_COLOR_BG_LIST_APP = "COLOR_BG_LIST_APP"
    private val PREF_LIST_APP = "LIST_APP"
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

    fun writeAppListInPref(apps: ArrayList<AppModel>) {
        val packageNameListSB = StringBuilder()
        for (appModel in apps) {
            packageNameListSB.append(appModel.getPackageName())
            packageNameListSB.append(";")
        }

        val  sharePreferences : SharedPreferences = SimpleLauncherApp.instance.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharePreferences.edit()
        editor.putString(PREF_LIST_APP, packageNameListSB.toString())
        editor.apply()
    }

    fun getAppsInPref(): List<String> {
        val  sharePreferences : SharedPreferences = SimpleLauncherApp.instance.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val packageNameList = sharePreferences.getString(PREF_LIST_APP, "")
        return packageNameList.split(";")
    }
}