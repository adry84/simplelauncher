package adrygraph.simplelauncher

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Audrey on 10/09/2017.
 * View model fro App
 */
class AppModelViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val nameTV: TextView
    val iconIV : ImageView

    init {
        nameTV = itemView!!.findViewById(R.id.itemAppLabelTV)
        iconIV = itemView.findViewById(R.id.itemAppIconIV)
    }
}