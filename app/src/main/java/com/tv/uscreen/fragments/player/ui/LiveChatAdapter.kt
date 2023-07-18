package com.tv.uscreen.fragments.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tv.uscreen.beanModel.pubnub.PNMessage
import com.tv.uscreen.databinding.ItemLiveChatBinding
import com.tv.uscreen.utils.ObjectHelper
import com.tv.uscreen.utils.TimeUtil


class LiveChatAdapter : RecyclerView.Adapter<LiveChatAdapter.ItemVH>() {
    companion object {
        private const val PAYLOAD_UPDATE_TIME = "payload_update_time"
    }

    private val messages: ArrayList<PNMessage> = ArrayList()

    fun addMessages(messages: List<PNMessage>) {
        if (ObjectHelper.isNotEmpty(messages)) {
            this.messages.addAll(0, messages)
            notifyItemRangeInserted(0, ObjectHelper.getSize(messages))
        }
    }

    fun addMessage(message: PNMessage) {
        this.messages.add(0, message)
        notifyItemInserted(0)
    }

    fun updateTimeStamp() {
        val size = ObjectHelper.getSize(messages)
        if (size > 1) {
            notifyItemRangeChanged(0, size, PAYLOAD_UPDATE_TIME)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val binding =
            ItemLiveChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemVH(binding)
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.setup(messages[position])
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int, payloads: MutableList<Any>) {
        if (ObjectHelper.isEmpty(payloads)) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (ObjectHelper.isSame(PAYLOAD_UPDATE_TIME, payloads[0].toString())) {
                holder.updateTime(messages[position])
            } else {
                super.onBindViewHolder(holder, position, payloads)
            }
        }
    }

    override fun getItemCount(): Int = ObjectHelper.getSize(this.messages)

    inner class ItemVH(private val binding: ItemLiveChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setup(message: PNMessage) {
            binding.apply {
                tvMessage.text = message.message
                tvSenderName.text = message.user
            }
            updateTime(message)
        }

        fun updateTime(message: PNMessage) {
            val timeLabel = TimeUtil.getTimeLabel(binding.tvSentTime.context, message.time)
            binding.tvSentTime.text = timeLabel
        }
    }
}