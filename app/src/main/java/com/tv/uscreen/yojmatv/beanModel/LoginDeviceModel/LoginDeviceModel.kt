package com.tv.uscreen.yojmatv.beanModel.LoginDeviceModel

class LoginDeviceModel {
    var data: Data? = null
    var responseCode = 0
    var debugMessage: Any? = null
}

class Data {
    var id = 0
    var deviceId: String? = null
    var deviceName: String? = null
    var deviceOS: String? = null
    var lastLogin: Long = 0
    var deviceType: String? = null
    var status: String? = null
    var lastAccessTime: Any? = null
}