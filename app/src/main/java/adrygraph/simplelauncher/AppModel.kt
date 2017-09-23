package adrygraph.simplelauncher

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat

/**
 * Created by Audrey on 10/09/2017.
 */
class AppModel(private val context: Context, private val appInfo: ApplicationInfo) {
    fun  loadLabel(context: Context?) {}
    fun  getLabel(): String? {
        val name = SimpleLauncherApp.instance.packageManager.getApplicationLabel(appInfo)
        if (name == null) {
            return ""
        }
        return name as String?
    }

    fun getIcon(): Drawable? {
        val icon = SimpleLauncherApp.instance.packageManager.getApplicationIcon(appInfo)
        if (icon == null) {
            return ContextCompat.getDrawable(context, R.drawable.ic_launcher_background)
        }
        return icon
    }

    fun getApplicationPackageName(): String? {
        return appInfo.packageName
    }

}