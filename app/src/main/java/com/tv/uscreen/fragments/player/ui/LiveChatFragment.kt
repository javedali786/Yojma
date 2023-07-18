package com.tv.uscreen.fragments.player.ui

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tv.uscreen.R

import com.tv.uscreen.activities.live.LiveActivity
import com.tv.uscreen.baseModels.BaseBindingFragment
import com.tv.uscreen.beanModel.pubnub.PNMessage
import com.tv.uscreen.databinding.FragmentLiveChatBinding
import com.tv.uscreen.utils.ObjectHelper


class LiveChatFragment: BaseBindingFragment<FragmentLiveChatBinding>() {

    private var liveChatAdapter: LiveChatAdapter? = null

    override fun inflateBindingLayout(inflater: LayoutInflater): FragmentLiveChatBinding {
        return FragmentLiveChatBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ImageViewCompat.setImageTintList(
            binding.ivClose, ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireActivity(), R.color.series_detail_description_text_color
                )
            )
        )

        binding.ivClose.setOnClickListener {
            if (activity is LiveActivity) {
               // (activity as LiveActivity).removeLiveChatFragment()
            }
        }

        liveChatAdapter = LiveChatAdapter()

        if (arguments?.containsKey("messages") == true) {
            val messages: ArrayList<PNMessage> = arguments?.getParcelableArrayList("messages") ?: ArrayList()
            liveChatAdapter?.addMessages(messages)
        }

        binding.rvChat.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            val dividerItemDecoration = DividerItemDecoration(
                context,
                linearLayoutManager.orientation
            )
            dividerItemDecoration.setDrawable(
                ColorDrawable(ContextCompat.getColor(context, R.color.order_history_title_color))
            )
            addItemDecoration(dividerItemDecoration)
            adapter = liveChatAdapter
        }

        binding.ivSendMessage.setOnClickListener {
            if (ObjectHelper.isNotEmpty(binding.etMessage) && activity is LiveActivity) {
               // (activity as LiveActivity).sendMessage(ObjectHelper.getText(binding.etMessage))
                binding.etMessage.setText("")
            }
        }
    }

    fun addNewMessage(message: PNMessage) {
        liveChatAdapter?.addMessage(message)
        binding.rvChat.smoothScrollToPosition(0)
        liveChatAdapter?.updateTimeStamp()
    }

    override fun onDestroyView() {
        liveChatAdapter = null
        super.onDestroyView()
    }
}