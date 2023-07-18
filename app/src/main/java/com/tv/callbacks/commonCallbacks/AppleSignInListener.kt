package com.tv.callbacks.commonCallbacks

interface AppleSignInListener {
    fun onAppSignInSuccess(jwt_token:String)
    fun onAppSignInError()
}