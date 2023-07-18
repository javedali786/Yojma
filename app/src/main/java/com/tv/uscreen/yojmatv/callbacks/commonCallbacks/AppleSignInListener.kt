package com.tv.uscreen.yojmatv.callbacks.commonCallbacks

interface AppleSignInListener {
    fun onAppSignInSuccess(jwt_token:String)
    fun onAppSignInError()
}