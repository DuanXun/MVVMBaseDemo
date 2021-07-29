package com.enzore.libbase.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.enzore.common.kt.util.toast
import com.safframework.utils.isConnected

/**
 *Des:
 *Time:2021/4/17 - 22:11
 *Author:enzore
 */
class NetworkStateReceive : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action==ConnectivityManager.CONNECTIVITY_ACTION){
                if (isConnected()){
                    "网络不给力~".toast()
                }
            }
        }
    }
}