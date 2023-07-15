package com.breadgangtvnetwork.callbacks.commonCallbacks

interface AppleSignInListener {
    fun onAppSignInSuccess(jwt_token:String)
    fun onAppSignInError()
}