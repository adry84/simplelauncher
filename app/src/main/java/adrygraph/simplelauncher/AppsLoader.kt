package adrygraph.simplelauncher

import android.content.pm.ApplicationInfo
import android.os.AsyncTask
import java.text.Collator
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Audrey on 10/09/2017.
 */
class AppsLoader(val listener: (ArrayList<AppModel>) -> Unit) : AsyncTask<Void, Void, ArrayList<AppModel>>() {

    override fun doInBackground(vararg p0: Void?): ArrayList<AppModel> {
        val context = SimpleLauncherApp.instance

        var apps = context.packageManager.getInstalledApplications(0)

        if (apps == null) {
            apps = ArrayList<ApplicationInfo>()
        }

        // create corresponding apps and load their labels
        val items = ArrayList<AppModel>(apps!!.size)
        for (i in 0..apps!!.size - 1) {
            val pkg = apps!![i].packageName

            // only apps which are launchable
            if (context.packageManager.getLaunchIntentForPackage(pkg) != null) {
                val app = AppModel(context, apps!![i])
                app.loadLabel(context)
                items.add(app)
            }
        }

        // sort the list
        Collections.sort(items, ALPHA_COMPARATOR)
        return items
    }

    override fun onPostExecute(result: ArrayList<AppModel>?) {
        super.onPostExecute(result)
        if (isCancelled) {
            return;
        }
       if (result != null) {
           listener(result);
       }
    }
}


/**
 * Perform alphabetical comparison of application entry objects.
 */
val ALPHA_COMPARATOR: Comparator<AppModel> = object : Comparator<AppModel> {
    private val sCollator = Collator.getInstance()
    override fun compare(object1: AppModel, object2: AppModel): Int {
        return sCollator.compare(object1.getLabel(), object2.getLabel())
    }
}
