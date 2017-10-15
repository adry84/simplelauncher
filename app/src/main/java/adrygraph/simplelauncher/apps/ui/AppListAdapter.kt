package adrygraph.simplelauncher.apps.ui

import adrygraph.simplelauncher.AppData
import adrygraph.simplelauncher.R
import adrygraph.simplelauncher.apps.models.AppModel
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*


/**
 * Created by Audrey on 10/09/2017.
 * AppListAdapter
 */
class AppListAdapter(private var listener: AppsGridItemListener) : RecyclerView.Adapter<AppModelViewHolder>(){

    private var list: ArrayList<AppModel> = ArrayList()


    override fun onBindViewHolder(holder: AppModelViewHolder?, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val app : AppModel = list[position]
        holder!!.nameTV.text = app.getLabel()
        holder.iconIV.setImageDrawable(app.getIcon())
        holder.itemView.setOnClickListener({
            listener.onAppModelClick(app)
        })
        holder.itemView.setOnLongClickListener {
            listener.onAppModelLongClick(app)
            true
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AppModelViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(parent?.context)
        val v = inflater.inflate(R.layout.item_app, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return AppModelViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(data: ArrayList<AppModel>?) {
        list.clear()
        if (data != null) {
            list.addAll(data)
        }
    }

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(list, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(list, i, i - 1)
            }
        }
        AppData.writeAppListInPref(list)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }
}