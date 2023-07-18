package com.tv.beanModel.pubnub

import android.os.Parcel
import android.os.Parcelable

class PNMessage() : Comparable<PNMessage>, Parcelable {
    var message: String = ""
    var type: String = "text"
    var user: String = ""
    var time: Long = 0L

    constructor(parcel: Parcel) : this() {
        message = parcel.readString() ?: ""
        type = parcel.readString() ?: ""
        user = parcel.readString() ?: ""
        time = parcel.readLong()
    }

    override fun compareTo(other: PNMessage): Int {
        return other.time.compareTo(this.time)
    }

    override fun toString(): String {
        return "PNMessage(message='$message', type='$type', user='$user', time=$time)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeString(type)
        parcel.writeString(user)
        parcel.writeLong(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PNMessage> {
        override fun createFromParcel(parcel: Parcel): PNMessage {
            return PNMessage(parcel)
        }

        override fun newArray(size: Int): Array<PNMessage?> {
            return arrayOfNulls(size)
        }
    }
}

class PubnubKeys {
    companion object {
        const val MSG_TYPE_TEXT = "text"
        const val KEY_TEXT = "text"
        const val KEY_USER = "user"
        const val KEY_TYPE = "type"
    }
}