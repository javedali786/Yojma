package com.tv.uscreen.yojmatv.fragments.dialog


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.baseModels.BaseActivity
import com.tv.uscreen.yojmatv.databinding.CustomPopupBinding
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.AppColors
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class CommonDialogFragment : DialogFragment() {
    private var binding: CustomPopupBinding? = null
    private var editDialogListener: EditDialogListener? = null
    private var baseActivity: BaseActivity? = null
    private val colorsHelper by lazy { ColorsHelper }
    private val stringsHelper by lazy { StringsHelper }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as BaseActivity
    }

    fun setEditDialogCallBack(editDialogListener: EditDialogListener?) {
        this.editDialogListener = editDialogListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup, null, false)
        if (dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.RED))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(false)
            val titleValue = requireArguments().getString(AppConstants.TITLE)
            val message = requireArguments().getString(AppConstants.MESSAGE)
            val actionBtn = requireArguments().getString(AppConstants.ACTION_BTN)
            parseColor()
            val btnOk = binding?.personalizeBtn
            val cancel = binding?.cancelButton
            val description = binding?.popupDiscription
            val title = binding?.popupTitle
            title?.text = titleValue
            if (titleValue!!.isEmpty()) {
                title?.visibility = View.GONE
            }
            if (titleValue.contains(requireContext().getString(R.string.log_out))) {
                cancel?.visibility = View.VISIBLE
            }
            //  cancel.setVisibility(View.GONE);
            if (titleValue.contains(requireContext().getString(R.string.change_preferences))) {
                cancel?.visibility = View.VISIBLE
            } //  cancel?.setVisibility(View.GONE);
            if (titleValue.contains(requireContext().getString(R.string.lang_change_lang))) {
                cancel?.visibility = View.VISIBLE
            } //  cancel?.setVisibility(View.GONE);
            if (titleValue.contains("Delete Account")) {
                cancel?.visibility = View.VISIBLE
            } //  cancel?.setVisibility(View.GONE);
            if (titleValue.contains("Under Review")) {
                cancel?.visibility = View.GONE
            } //  cancel?.setVisibility(View.GONE);
            if (titleValue.contains("Cancel Subscription")) {
                cancel?.visibility = View.VISIBLE
            } //cancel?.setVisibility(View.GONE);
            if (titleValue.contains("Change Language")) {
                cancel?.visibility = View.VISIBLE
            } // cancel?.setVisibility(View.GONE);
            if (titleValue.contains("Change Preferences")) {
                cancel?.visibility = View.VISIBLE
            } // cancel.setVisibility(View.GONE);

            /* if (title_value.contains(title_value)) {
                cancel.setVisibility(View.VISIBLE);
            }*/if (titleValue.contains("Recent Searches")) {
                cancel?.visibility = View.VISIBLE
                cancel?.text = "No"
            }
            if (message!!.isEmpty()) {
                description?.visibility = View.GONE
            }
            if (message.contains(resources.getString(R.string.select_plan))) {
                cancel?.visibility = View.VISIBLE
            }
            if (message.contains(resources.getString(R.string.user_not_verify))) {
                cancel?.visibility = View.VISIBLE
            }
            if (message.contains(resources.getString(R.string.delete_hunting))) {
                cancel?.visibility = View.VISIBLE
            }
            description?.text = message
            btnOk?.text = actionBtn
            btnOk?.setOnClickListener { v: View? ->
                if (editDialogListener != null) editDialogListener!!.onActionBtnClicked()
                dismiss()
            }
            cancel?.setOnClickListener { v: View? -> dismiss() }
            dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }
        return binding?.root
    }

    private fun parseColor() {
        binding?.colorsData = colorsHelper
        binding?.stringData = stringsHelper
        binding?.llNumber?.background = ColorsHelper.strokeBgDrawable(AppColors.popupBgColor(), AppColors.popupBrColor(), 10f)
        binding?.cancelButton?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
    }

    override fun onResume() {
        if (dialog?.window != null) dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        super.onResume()
    }

    interface EditDialogListener {
        fun onActionBtnClicked()
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String?, message: String?, actnBtn: String?): CommonDialogFragment {
            val frag = CommonDialogFragment()
            val args = Bundle()
            args.putString(AppConstants.TITLE, title)
            args.putString(AppConstants.MESSAGE, message)
            args.putString(AppConstants.ACTION_BTN, actnBtn)
            frag.arguments = args
            return frag
        }
    }
}