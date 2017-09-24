package adrygraph.simplelauncher.apps.ui

import adrygraph.simplelauncher.R
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Audrey on 10/09/2017.
 * View model fro App
 */
class AppModelViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val nameTV: TextView = itemView!!.findViewById(R.id.itemAppLabelTV)
    val iconIV : ImageView = itemView!!.findViewById(R.id.itemAppIconIV)
}