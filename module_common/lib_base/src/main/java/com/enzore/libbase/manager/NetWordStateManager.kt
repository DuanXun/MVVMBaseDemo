package com.enzore.libbase.manager

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.safframework.utils.getApplicationContext

/**
 *Des:
 *Time:2021/4/17 - 21:50
 *Author:enzore
 */
class NetWordStateManager : DefaultLifecycleObserver{

    lateinit var networkStateReceive:NetworkStateReceive
    companion object{
        fun get() = NetWordStateManager()
    }

    override fun onResume(owner: LifecycleOwner) {
        networkStateReceive = NetworkStateReceive()
        var filter: IntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        getApplicationContext().registerReceiver(networkStateReceive,filter)
    }

    override fun onPause(owner: LifecycleOwner) {
        getApplicationContext().unregisterReceiver(networkStateReceive)
    }

}