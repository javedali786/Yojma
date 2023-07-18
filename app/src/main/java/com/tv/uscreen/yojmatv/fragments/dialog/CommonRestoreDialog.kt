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
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.tv.uscreen.yojmatv.R


import com.tv.uscreen.yojmatv.baseModels.BaseActivity
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class CommonRestoreDialog : DialogFragment() {
    private var editDialogListener: EditDialogListener? = null
    private var baseActivity: BaseActivity? = null
    private val stringsHelper by lazy { StringsHelper }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as BaseActivity
    }

    fun setEditDialogCallBack(editDialogListener: EditDialogListener?) {
        this.editDialogListener = editDialogListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.custom_popup, container)
        //ThemeHandler.getInstance().applyPopUp(requireContext(),view);
        if (dialog?.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.RED))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(false)
            val title_value = requireArguments().getString(AppConstants.TITLE)
            var message: String? = null
            if (arguments != null) {
                message = requireArguments().getString(AppConstants.MESSAGE)
                val actnBtn = requireArguments().getString(AppConstants.ACTION_BTN)
            }
            val btnOk = view.findViewById<Button>(R.id.personalizeBtn)
            val cancel = view.findViewById<Button>(R.id.cancelButton)
            val description = view.findViewById<TextView>(R.id.popup_discription)
            val title = view.findViewById<TextView>(R.id.popup_title)
            title.text = title_value
            if (title_value!!.isEmpty()) {
                title.visibility = View.GONE
            }
            description.text = message
            if (typeOfDialog == 1) {
                btnOk.text = stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue))
            } else if (typeOfDialog == 2) {
                btnOk.text = stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_ok.toString(), getString(R.string.popup_ok))
            } else {
                btnOk.text = stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_ok.toString(), getString(R.string.popup_ok))
            }
            btnOk.setOnClickListener { v: View? ->
                if (editDialogListener != null) {
                    if (typeOfDialog == 1) {
                        editDialogListener!!.onRestoreActionBtnClicked(1)
                        dismiss()
                    } else if (typeOfDialog == 2) {
                        editDialogListener!!.onRestoreActionBtnClicked(2)
                        dismiss()
                    } else {
                        editDialogListener!!.onRestoreActionBtnClicked(3)
                        dismiss()
                    }
                }
            }
            cancel.setOnClickListener { v: View? -> dismiss() }
            dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }
        return view
    }

    override fun onResume() {
        if (dialog?.window != null) dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        super.onResume()
    }

    interface EditDialogListener {
        fun onActionBtnClicked()
        fun onRestoreActionBtnClicked(type: Int) {}
    }

    companion object {
        var typeOfDialog = 2
        fun newInstance(title: String?, message: String?, type: Int): CommonRestoreDialog {
            typeOfDialog = type
            val frag = CommonRestoreDialog()
            val args = Bundle()
            args.putString(AppConstants.TITLE, title)
            args.putString(AppConstants.MESSAGE, message)
            frag.arguments = args
            return frag
        }
    }
}