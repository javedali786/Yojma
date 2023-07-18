package com.tv.uscreen.callbacks.commonCallbacks

interface AppleSignInListener {
    fun onAppSignInSuccess(jwt_token:String)
    fun onAppSignInError()
}