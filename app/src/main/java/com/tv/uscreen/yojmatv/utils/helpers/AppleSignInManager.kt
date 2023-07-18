package com.tv.uscreen.yojmatv.utils.helpers

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.AppleSignInListener
import java.lang.ref.WeakReference

object AppleSignInManager {
    private lateinit var appledialog: Dialog
    private lateinit var appleAuthURL: String
    private lateinit var mcontext: WeakReference<Activity>
    private var appleSignInListener: AppleSignInListener? = null
    private var appleFirstName = ""

    private var appleLoginRedirectURI = ""
    fun setUrl(authUrl: String) {
        appleSignInListener = mcontext.get() as AppleSignInListener;
        appleAuthURL = authUrl
    }

    fun setContext(activity: Activity) {
        mcontext = WeakReference(activity);
    }

    fun setupAppleWebViewDialog() {
        appledialog = Dialog(mcontext.get() as Context)
        val webView = WebView(mcontext.get() as Context)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = AppleWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(appleAuthURL)
        appledialog.setContentView(webView)
        appledialog.show()
    }

    @Suppress("OverridingDeprecatedMember")
    class AppleWebViewClient : WebViewClient() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            handleUrl(request?.url.toString())
            if (request?.url.toString().contains("success=")) {
                appledialog.dismiss()
            }
            return true
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(appleLoginRedirectURI)) {
                handleUrl(url)
                if (url.contains("success=")) {
                    appledialog.dismiss()
                }
                return true
            }
            return false
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            val displayRectangle = Rect()
            val window = (mcontext.get() as Activity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            val layoutParams = view?.layoutParams
            layoutParams?.height = (displayRectangle.height() * 0.9f).toInt()
            view?.layoutParams = layoutParams
        }

        @SuppressLint("LongLogTag")
        private fun handleUrl(url: String) {
            val uri = Uri.parse(url)
            val success = uri.getQueryParameter("success")
            if (success == "true") {
                val jwt_token = uri.getQueryParameter("at")
                if (jwt_token != null) {
                    appleSignInListener?.onAppSignInSuccess(
                        jwt_token
                    )

                } else {
                    appleSignInListener?.onAppSignInError()
                }
            } else if (success == "false") {
                appleSignInListener?.onAppSignInError()
            }else{
                appleSignInListener?.onAppSignInError()

            }
        }
    }
}
