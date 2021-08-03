package com.enzore.mvvmbasedemo.model.core.net

import com.dawn.kotlinbasedemo.http.getApi
import com.enzore.mvvmbasedemo.BuildConfig
import com.enzore.mvvmbasedemo.model.core.net.apiservice.ApiService

val baseApi by getApi(HttpConfig.BASE_URL,ApiService::class.java)
//此处可以添加多个baseUrl的路径接口
//val actApi by getApi(HttpConfig.ACT_URL,ActInterface::class.java)

object HttpConfig {

    val BASE_URL:String
    get() {
        if (BuildConfig.DEBUG)
            return "https://www.wanandroid.com/"
        else
            return "https://www.wanandroid.com/"
    }

    /*val ACT_URL:String
        get() {
            if (BuildConfig.DEBUG)
                return "https://cxcxevent.ttdianjing.com/"
            else
                return "https://tjy.ttdianjing.com/"
        }*/
}