package com.safframework.utils

import android.Manifest.permission
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import androidx.annotation.RequiresPermission
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

/**
 *
 * @FileName:
 *          com.safframework.utils.NetUtils
 * @author: Tony Shen
 * @date: 2020-11-19 15:02
 * @version: V1.0 <描述当前版本功能>
 */

/**
 * 获取内网IP地址
 */
val localIPAddress: String by lazy {

    val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
    while (en.hasMoreElements()) {
        val intf: NetworkInterface = en.nextElement()
        val enumIpAddr: Enumeration<InetAddress> = intf.inetAddresses
        while (enumIpAddr.hasMoreElements()) {
            val inetAddress: InetAddress = enumIpAddr.nextElement()
            if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                return@lazy inetAddress.hostAddress.toString()
            }
        }
    }
    "null"
}

/**
 * 获取局域网的网关地址
 */
fun getGatewayIP(context: Context):String {

    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    val info = wifiManager.dhcpInfo
    val gateway = info.gateway

    return intToIp(gateway)
}

/**
 * int值转换为ip
 * @param addr
 * @return
 */
private fun intToIp(addr: Int): String {
    var addr = addr
    return (addr and 0xFF).toString() + "." +
            (8.let { addr = addr ushr it; addr } and 0xFF) + "." +
            (8.let { addr = addr ushr it; addr } and 0xFF) + "." +
            (8.let { addr = addr ushr it; addr } and 0xFF)
}

@RequiresPermission(permission.ACCESS_NETWORK_STATE)
fun isConnected():Boolean{
    val info:NetworkInfo? = getActivityNetWorkInfo()
    return info != null && info.isConnected()
}

@RequiresPermission(permission.ACCESS_NETWORK_STATE)
private fun getActivityNetWorkInfo():NetworkInfo?{
    val cm:ConnectivityManager =
        getApplicationContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm?.activeNetworkInfo
}