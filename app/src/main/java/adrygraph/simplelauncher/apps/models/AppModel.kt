package adrygraph.simplelauncher.apps.models

import adrygraph.simplelauncher.R
import adrygraph.simplelauncher.SimpleLauncherApp
import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat

/**
 * Created by Audrey on 10/09/2017.
 * Android Application model
 */
class AppModel(private val context: Context, private val appInfo: ApplicationInfo) {
    fun  loadLabel() {}
    fun  getLabel(): String? {
        val name = SimpleLauncherApp.instance.packageManager.getApplicationLabel(appInfo) ?: return ""
        return name as String?
    }

    fun  getPackageName(): String {
        return appInfo.packageName
    }

    fun getIcon(): Drawable? {
        val icon = SimpleLauncherApp.instance.packageManager.getApplicationIcon(appInfo) ?: return ContextCompat.getDrawable(context, R.drawable.ic_launcher_background)
        return icon
    }

    fun getApplicationPackageName(): String? {
        return appInfo.packageName
    }

}