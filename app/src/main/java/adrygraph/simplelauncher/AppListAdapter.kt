package adrygraph.simplelauncher

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*


/**
 * Created by Audrey on 10/09/2017.
 * AppListAdapter
 */
class AppListAdapter(context: Context, resource: Int, val listener: (AppModel) -> Unit) : RecyclerView.Adapter<AppModelViewHolder>(){

    var context: Context
    var resource: Int
    var list: ArrayList<AppModel>
    var vi: LayoutInflater

    init {
        this.context = context
        this.resource = resource
        this.list = ArrayList()
        this.vi =  LayoutInflater.from(context)
    }


    override fun onBindViewHolder(holder: AppModelViewHolder?, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val app : AppModel
        app = list[position]
        holder!!.nameTV.text = app.getLabel()
        holder.iconIV.setImageDrawable(app.getIcon())
        holder.itemView.setOnClickListener({
            listener(app)
        })
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
            list. addAll(data)
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
        notifyItemMoved(fromPosition, toPosition)
        return true
    }
}