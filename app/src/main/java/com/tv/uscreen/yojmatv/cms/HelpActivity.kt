package com.tv.uscreen.yojmatv.cms

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.tv.uscreen.yojmatv.R


import com.tv.uscreen.yojmatv.SDKConfig
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.databinding.ActivityHelpBinding

import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.helpers.ToastHandler
import com.tv.uscreen.yojmatv.utils.helpers.ToolBarHandler
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class HelpActivity : BaseBindingActivity<ActivityHelpBinding?>() {
    private var type: String? = null
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityHelpBinding {
        return ActivityHelpBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = intent.getStringExtra("type")
        if (type.equals("1", ignoreCase = true)) {
            ToolBarHandler.getInstance().setHelpAction(binding, this@HelpActivity.resources.getString(R.string.terms_and_conditions), this)
        } else if (type.equals("2", ignoreCase = true)) {
            ToolBarHandler.getInstance().setHelpAction(binding, this@HelpActivity.resources.getString(R.string.privacy_policy), this)
        } else if (type.equals("3", ignoreCase = true)) {
            ToolBarHandler.getInstance().setHelpAction(binding, this@HelpActivity.resources.getString(R.string.more_contact_us), this)
        } else if (type.equals("4", ignoreCase = true)) {
            ToolBarHandler.getInstance().setHelpAction(binding, this@HelpActivity.resources.getString(R.string.faq), this)
        } else if (type.equals("5", ignoreCase = true)) {
            ToolBarHandler.getInstance().setHelpAction(binding, this@HelpActivity.resources.getString(R.string.about_us), this)
        } else if (type.equals("6", ignoreCase = true)) {
            ToolBarHandler.getInstance().setHelpAction(binding, this@HelpActivity.resources.getString(R.string.feedback), this)
        } else if (type.equals("7", ignoreCase = true)) {
            ToolBarHandler.getInstance().setHelpAction(binding, this@HelpActivity.resources.getString(R.string.other_application), this)
        } else if (type.equals("8", ignoreCase = true)) {
            ToolBarHandler.getInstance().setHelpAction(binding, this@HelpActivity.resources.getString(R.string.help_center), this)
        }
        binding!!.toolbarFaq.titleSkip.visibility = View.GONE
        binding!!.toolbarFaq.llSearchIcon.visibility = View.GONE
        binding!!.toolbarFaq.logoMain2.visibility = View.GONE
        binding!!.toolbarFaq.colorsData = ColorsHelper
        binding!!.toolbarFaq.stringData = StringsHelper
        binding!!.toolbarFaq.backLayout.setOnClickListener { onBackPressed() }
        getwebView()
    }

    private fun getwebView() {
        binding!!.webView.settings.javaScriptEnabled = true
        binding!!.webView.settings.builtInZoomControls = true
        binding!!.webView.settings.useWideViewPort = true
        binding!!.webView.settings.loadWithOverviewMode = true
        binding!!.webView.settings.domStorageEnabled = true

        //  getBinding().webView.setBackgroundColor(getResources().getColor(R.color.theme_background_dark));
        val url: String = if (type.equals("1", ignoreCase = true)) {
          SDKConfig.getInstance().termCondition_URL
        } else if (type.equals("2", ignoreCase = true)) {
           SDKConfig.getInstance().privay_Policy_URL
        } else if (type.equals("3", ignoreCase = true)) {
            SDKConfig.getInstance().contacT_URL
        } else if (type.equals("4", ignoreCase = true)) {
            SDKConfig.getInstance().faQ_URL
        } else if (type.equals("5", ignoreCase = true)) {
            SDKConfig.getInstance().abouT_US_URL
        } else if (type.equals("6", ignoreCase = true)) {
            SDKConfig.getInstance().feedbacK_URL
        } else if (type.equals("8", ignoreCase = true)) {
            SDKConfig.getInstance().helP_CENTER_URL
        } else if (type.equals("7", ignoreCase = true)) {
            "https://play.google.com/store/apps/dev?id=7373346074773014663"
        } else {
            "https://www.google.co.in/"
        }
        Logger.e("LOAD ERROR: $url")
        binding!!.webView.loadUrl(url)
        binding!!.webView.settings.builtInZoomControls = false
        binding!!.webView.webViewClient = InsideWebViewClient()
    }

    private inner class InsideWebViewClient : WebViewClient() {
        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            super.onReceivedError(view, request, error)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return if (url.startsWith("mailto:")) {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse(url)
                startActivity(intent)
                view.reload()
                true
            } else {
                false
            }
        }

        override fun onLoadResource(view: WebView, url: String) {
            super.onLoadResource(view, url)
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            Logger.e("LOAD ERROR: $error")
            when (error.primaryError) {
                SslError.SSL_UNTRUSTED -> {}
                SslError.SSL_EXPIRED -> {}
                SslError.SSL_IDMISMATCH -> {}
                SslError.SSL_NOTYETVALID -> {}
            }
            handler.cancel()
            showErrorToast()
        }

        override fun onReceivedHttpError(view: WebView, request: WebResourceRequest, errorResponse: WebResourceResponse) {
            super.onReceivedHttpError(view, request, errorResponse)
        }

        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            super.onReceivedError(view, errorCode, description, failingUrl)
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return if (request.url.toString().startsWith("mailto:")) {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse(request.url.toString())
                startActivity(intent)
                view.reload()
                true
            } else {
                false
            }
        }
    }

    private fun showErrorToast() {
        try {
            runOnUiThread {
                ToastHandler.getInstance().show(this@HelpActivity, this@HelpActivity.resources.getString(R.string.something_went_wrong_at_our_end))
                onBackPressed()
            }
        } catch (e: Exception) {
            Logger.w(e)
        }
    }
}