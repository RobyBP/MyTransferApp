package com.robybp.mytransferapp.sms

import android.telephony.SmsManager

class SendSmsUseCase {
    fun sendMessage(messageBody: String, phoneNumber: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, messageBody, null, null)
    }
}
