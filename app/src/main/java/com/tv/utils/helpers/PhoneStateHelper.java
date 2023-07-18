package com.tv.utils.helpers;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.tv.callbacks.commonCallbacks.PhoneListenerCallBack;


public class PhoneStateHelper extends PhoneStateListener {

    private static PhoneStateHelper mInstance;
    private PhoneListenerCallBack mListener;

    private PhoneStateHelper() {

    }

    public static PhoneStateHelper getInstance() {
        if (mInstance == null)
            mInstance = new PhoneStateHelper();
        return mInstance;
    }

    public void setListener(PhoneListenerCallBack listener) {
        this.mListener = listener;
    }

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        if (state == TelephonyManager.CALL_STATE_RINGING) {
            mListener.onCallStateRinging();
        } else if (state == TelephonyManager.CALL_STATE_IDLE) {
            mListener.onCallStateIdle(state);
        } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
            //phoneListenerCallBack.onCallStateIdle(state);
            //A call is dialing, active or on hold
            //Log.d("njhhkk","khjggjgj");
        }
        super.onCallStateChanged(state, phoneNumber);
    }
}
