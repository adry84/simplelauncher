package adrygraph.simplelauncher

import android.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView




/**
 * Created by Audrey on 10/09/2017.
 * AppsGridFragment
 */
class AppsGridFragment : Fragment(), AppsGridItemListener{


    private var mAdapter: AppListAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAppInstallReceiver: BroadcastReceiver? = null
    private var mAppPopupWindow: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerAppReceiver()
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater?.inflate(R.layout.fragment_apps_grid, container, false)
        return rootView!!
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView = view?.findViewById(R.id.fragmentAppsGridRV)!!
        mRecyclerView?.layoutManager = GridLayoutManager(view.context, 4)
        mAdapter = AppListAdapter(this)
        mRecyclerView?.adapter = mAdapter

        loadApps()

        val itemTouchHelper = createItemTouchHelper()
        itemTouchHelper.attachToRecyclerView(mRecyclerView)
    }

    private fun loadApps() {
        AppsLoader({onLoadFinished(it)}).execute(null)
    }

    override fun onDestroy() {
        unregisterAppReceiver()
        super.onDestroy()
    }


     private fun onLoadFinished(apps: ArrayList<AppModel>) {
        if (mAdapter == null) {
            return
        }
        mAdapter?.setData(apps)
        mAdapter?.notifyDataSetChanged()
    }

    private fun createItemTouchHelper() : ItemTouchHelper{
        val simpleItemTouchCallback = object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.START or ItemTouchHelper.END)
            }

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
               dismissAppPopupWindow()
                mAdapter?.onItemMove(viewHolder!!.adapterPosition, target!!.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
            }

            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return true
            }
        }
        return ItemTouchHelper(simpleItemTouchCallback)
    }


    override fun onAppModelClick(app: AppModel) {
        val intent = SimpleLauncherApp.instance.packageManager.getLaunchIntentForPackage(app.getApplicationPackageName())

        if (intent != null) {
            startActivity(intent)
        }
    }

    override fun onAppModelLongClick(app: AppModel) {
        displayAppPopupWindow(app)
    }

    private fun registerAppReceiver() {
        if (mAppInstallReceiver != null) {
            return
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        @Suppress("DEPRECATION")
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addDataScheme("package")
        mAppInstallReceiver =  object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                if (mAdapter == null) {
                    return
                }
                loadApps()
            }
        }
        SimpleLauncherApp.instance.registerReceiver(mAppInstallReceiver, intentFilter)
    }

    private fun unregisterAppReceiver() {
        if (mAppInstallReceiver == null) {
            return
        }
        SimpleLauncherApp.instance.unregisterReceiver(mAppInstallReceiver)
    }

    private fun displayAppPopupWindow(app: AppModel) {
        if (activity == null) {
            return
        }
        val inflater = LayoutInflater.from(activity)
        //Inflate the view from a predefined XML layout
        val layout = inflater.inflate(R.layout.popup_app_layout, mRecyclerView, false)

        val displayMetrics = activity.resources.displayMetrics
        // create a 300px width and 470px height PopupWindow
        mAppPopupWindow = PopupWindow(layout, displayMetrics.widthPixels, resources.getDimensionPixelSize(R.dimen.item_app_popup_height), true)
        // display the popup in the center
        mAppPopupWindow!!.showAtLocation(layout, Gravity.TOP, 0, 0)

        val uninstallButton : Button
        uninstallButton = layout?.findViewById(R.id.popupAppUninstallB)!!
        val infoButton : Button
        infoButton= layout.findViewById(R.id.popupAppInfoB)!!

        val nameTV : TextView
        nameTV= layout.findViewById(R.id.popupAppNameTV)!!
        nameTV.text = app.getLabel()

        uninstallButton.setOnClickListener({
            val packageURI = Uri.parse("package:" + app.getApplicationPackageName())
            val uninstallIntent = Intent(Intent.ACTION_DELETE, packageURI)
            startActivity(uninstallIntent)
            dismissAppPopupWindow()
        })
        infoButton.setOnClickListener({
            val i = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            i.addCategory(Intent.CATEGORY_DEFAULT)
            i.data = Uri.parse("package:" + app.getApplicationPackageName())
            startActivity(i)
            dismissAppPopupWindow()
        })
    }

    private fun dismissAppPopupWindow() {
        if (mAppPopupWindow != null) {
            mAppPopupWindow!!.dismiss()
        }
    }
}
