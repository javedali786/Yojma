package com.example.jwplayer.VideoQualityAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jwplayer.pub.api.media.audio.AudioTrack
import com.tv.uscreen.yojmatv.R


import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys

class AudioAdapter(
    var mList: ArrayList<AudioTrack>,
    itemClick: ItemClick?,
    private val receivedAudioTrackList: String?,
    defaultLanguage: String?,
    primaryLanguage: String?,
    isDefaultLanguageEnable: Boolean
) : RecyclerView.Adapter<AudioAdapter.ViewHolder>() {
     var itemClick = itemClick
    var defaultLanguage = defaultLanguage
    var primaryLanguage = primaryLanguage
    var isDefaultLanguageEnable = isDefaultLanguageEnable

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
        Log.d("ListLanguageIs", "onBindViewHolder: " + mList?.get(position)!!.name)

        if (KsPreferenceKeys.getInstance().audioName != "" && KsPreferenceKeys.getInstance().audioName != null) {
            if (mList.get(position).name.equals(KsPreferenceKeys.getInstance().audioName, ignoreCase = true)) {
                if (KsPreferenceKeys.getInstance().audioName.equals("Default", ignoreCase = true)) {
                    holder.titleText.text = primaryLanguage
                }else{
                    holder.titleText.text = mList.get(position).name
                }
                holder.titleText.setBackgroundResource(R.drawable.ic_rectangle_background_selected_blue)
                holder.titleText.setTextColor(holder.titleText.context.resources.getColor(R.color.moe_white));
            }else{
                if (mList.get(position).name.equals("Default", ignoreCase = true)) {
                    holder.titleText.text = primaryLanguage
                }else {
                    holder.titleText.text = mList.get(position).name
                }
                holder.titleText.setBackgroundResource(R.drawable.ic_rectangle_background_selected)
                holder.titleText.setTextColor(holder.titleText.context.resources.getColor(R.color.buy_now_pay_now_btn_text_color));
            }
        }else {
            if (mList.get(position).name.equals("Default", ignoreCase = true)) {
                if (!isDefaultLanguageEnable){
                    holder.titleText.setBackgroundResource(R.drawable.ic_rectangle_background_selected_blue)
                    holder.titleText.setTextColor(holder.titleText.context.resources.getColor(R.color.moe_white));
                }
                 holder.titleText.text = primaryLanguage
                //holder.titleText.setTextColor(holder.titleText.context.resources.getColor(R.color.moe_white));
            } else {
                if (defaultLanguage.equals(mList.get(position).name ,ignoreCase = true)) {
                    holder.titleText.text = mList.get(position).name
                    itemClick?.initialAudioSelected(position)
                    holder.titleText.setBackgroundResource(R.drawable.ic_rectangle_background_selected_blue)
                    holder.titleText.setTextColor(holder.titleText.context.resources.getColor(R.color.moe_white));
                } else {
                    holder.titleText.text = mList.get(position).name
                    holder.titleText.setBackgroundResource(R.drawable.ic_rectangle_background_selected)
                    holder.titleText.setTextColor(holder.titleText.context.resources.getColor(R.color.buy_now_pay_now_btn_text_color));
                }

            }
        }
     //   holder.titleText.text = mList?.get(position)?.name
//        if (mList?.get(position)?.name.equals(KsPreferenceKeys.getInstance().audioName,true)) {
//            holder.titleText.setBackgroundResource(R.drawable.ic_rectangle_background_selected_blue)
//            holder.titleText.setTextColor(holder.titleText.context.resources.getColor(R.color.moe_white));
//        }else if(mList?.get(position)?.name.equals(KsPreferenceKeys.getInstance().audioName,true)){
//
//            }
//        else{
//            holder.titleText.setBackgroundResource(R.drawable.ic_rectangle_background_selected)
//            holder.titleText.setTextColor(holder.titleText.context.resources.getColor(R.color.buy_now_pay_now_btn_text_color));
//        }

        holder.titleText.setOnClickListener{
          itemClick?.itemClick(position)
            KsPreferenceKeys.getInstance().audioName=mList?.get(position)?.name
            Log.d("ClickedOperation", "onBindViewHolder: " + KsPreferenceKeys.getInstance().audioName)
            holder.titleText.setBackgroundResource(R.drawable.ic_rectangle_background_selected_blue)
            holder.titleText.setTextColor(holder.titleText.context.resources.getColor(R.color.moe_white));
            notifyItemChanged(position)
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
        fun itemClick(trackName: Int)
        fun initialAudioSelected(trackName: Int)
    }
}
