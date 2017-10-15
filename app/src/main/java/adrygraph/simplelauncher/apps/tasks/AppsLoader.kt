package adrygraph.simplelauncher.apps.tasks

import adrygraph.simplelauncher.AppData
import adrygraph.simplelauncher.SimpleLauncherApp
import adrygraph.simplelauncher.apps.models.AppModel
import android.content.pm.ApplicationInfo
import android.os.AsyncTask
import android.util.Log
import java.text.Collator
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Audrey on 10/09/2017.
 * AsyncTask to load app from android package manager
 */
class AppsLoader(private val listener: (ArrayList<AppModel>) -> Unit) : AsyncTask<Void, Void, ArrayList<AppModel>>() {

    override fun doInBackground(vararg p0: Void?): ArrayList<AppModel> {
        val context = SimpleLauncherApp.instance

        val appInfoList = context.packageManager.getInstalledApplications(0)
        val apps = ArrayList<ApplicationInfo>()
        val hasNoPrefs = appInfoList == null

        if (!hasNoPrefs) {
            val appInfoMap = appInfoList.associate({ Pair(it.packageName, it) })
            val sortedPackageNames = AppData.getAppsInPref()
            var appRemoved : Boolean
            for (appPackageName in sortedPackageNames) {
                val appInfo = appInfoMap.get(appPackageName)
                if (appInfo != null) {
                    apps.add(appInfo)
                    appRemoved = appInfoList.remove(appInfo)
                    Log.d("AppLoader", "app:" +appPackageName + " removed:"+ appRemoved)
                }
            }
            for (appInfo in appInfoList) {
                apps.add(appInfo)
            }
        }

        // create corresponding apps and load their labels
        val items = ArrayList<AppModel>(apps.size)
        for (i in 0 until apps.size) {
            val pkg = apps[i].packageName

            // only apps which are launchable
            if (context.packageManager.getLaunchIntentForPackage(pkg) != null) {
                val app = AppModel(context, apps[i])
                app.loadLabel()
                items.add(app)
            }
        }

        // sort the list
        if (hasNoPrefs) {
            Collections.sort(items, alphaComparator)
        }
        return items
    }

    override fun onPostExecute(result: ArrayList<AppModel>?) {
        super.onPostExecute(result)
        if (isCancelled) {
            return
        }
       if (result != null) {
           listener(result)
       }
    }
}


/**
 * Perform alphabetical comparison of application entry objects.
 */
val alphaComparator: Comparator<AppModel> = object : Comparator<AppModel> {
    private val sCollator = Collator.getInstance()
    override fun compare(object1: AppModel, object2: AppModel): Int {
        return sCollator.compare(object1.getLabel(), object2.getLabel())
    }
}
