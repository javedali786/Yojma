package com.example.jwplayer.VideoQualityAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enveu.player.model.TrackItem
import com.tv.R
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys

class CustomAdapter(private val mList: ArrayList<TrackItem>?, itemClick: ItemClick?, selectedTrack: String) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
     var itemClick = itemClick
     var selectedTrack = selectedTrack


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.quality_items, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.titleText.text = mList?.get(position)?.trackName
        if (mList?.get(position)?.trackName.equals(KsPreferenceKeys.getInstance().qualityName,true)) {
            holder.titleText.setBackgroundResource(R.drawable.ic_rectangle_background_selected)
            holder.titleText.setTextColor(holder.titleText.context.resources.getColor(R.color.buy_now_pay_now_btn_text_color));
        }else if(mList?.get(position)?.trackName.equals(KsPreferenceKeys.getInstance().qualityName,true)){

            }
        else{
            holder.titleText.setBackgroundResource(R.drawable.ic_rectangle_background)
            holder.titleText.setTextColor(holder.titleText.context.resources.getColor(R.color.series_detail_now_playing_title_color));
        }

        holder.titleText.setOnClickListener{
            mList?.get(position)?.id?.let { it1 -> itemClick?.itemClick(it1,mList.get(position).trackName) }
            KsPreferenceKeys.getInstance().qualityName=mList?.get(position)?.trackName
            holder.titleText.setBackgroundResource(R.drawable.ic_rectangle_background_selected)
            holder.titleText.setTextColor(holder.titleText.context.resources.getColor(R.color.buy_now_pay_now_btn_text_color));
        }


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList!!.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var titleText: TextView = itemView.findViewById(R.id.title_text)
    }

    interface ItemClick {
        fun itemClick(trackName: Int, userSelectedTrack: String)
    }
}
