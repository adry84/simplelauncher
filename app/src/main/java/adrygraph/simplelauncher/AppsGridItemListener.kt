package adrygraph.simplelauncher

/**
 * Created by Audrey on 23/09/2017.
 * interface between AppListAdapter and AppGridFragment
 */
interface AppsGridItemListener {
    fun onAppModelClick(app: AppModel)
    fun onAppModelLongClick(app: AppModel)
}