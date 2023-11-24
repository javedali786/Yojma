package com.tv.uscreen.yojmatv.callbacks.apicallback

import com.tv.uscreen.yojmatv.beanModel.entitle.ResponseEntitle

interface EntitlementCallBack {
    fun entitlementStatus(responseEntitle: ResponseEntitle)
}