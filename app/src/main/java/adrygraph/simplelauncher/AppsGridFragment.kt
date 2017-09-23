package adrygraph.simplelauncher

import android.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup




/**
 * Created by Audrey on 10/09/2017.
 * AppsGridFragment
 */
class AppsGridFragment : Fragment() { //

    private var mAdapter: AppListAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAppInstallReceiver: BroadcastReceiver? = null

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
        mRecyclerView?.layoutManager = GridLayoutManager(view?.context, 4) as RecyclerView.LayoutManager?
        mAdapter = AppListAdapter(activity, R.layout.item_app) {
            onAppModelClick(it)
        }
        mRecyclerView?.adapter = mAdapter

        loadApp()

        val itemTouchHelper = createItemTouchHelper()
        itemTouchHelper.attachToRecyclerView(mRecyclerView)
    }

    private fun loadApp() {
        AppsLoader({onLoadFinished(it)}).execute(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroy() {
        unregisterAppReceiver()
        super.onDestroy()
    }


     fun onLoadFinished(apps: ArrayList<AppModel>) {
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
                mAdapter?.onItemMove(viewHolder!!.adapterPosition, target!!.adapterPosition);
                return true;
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


    private fun onAppModelClick(app: AppModel) {
        val intent = SimpleLauncherApp.instance.packageManager.getLaunchIntentForPackage(app.getApplicationPackageName())

        if (intent != null) {
            startActivity(intent)
        }
    }

    fun registerAppReceiver() {
        if (mAppInstallReceiver != null) {
            return;
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addDataScheme("package")
        mAppInstallReceiver =  object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                if (mAdapter == null) {
                    return
                }
                loadApp()
            }
        }
        SimpleLauncherApp.instance.registerReceiver(mAppInstallReceiver, intentFilter)
    }

    fun unregisterAppReceiver() {
        if (mAppInstallReceiver == null) {
            return;
        }
        SimpleLauncherApp.instance.unregisterReceiver(mAppInstallReceiver)
    }
}
