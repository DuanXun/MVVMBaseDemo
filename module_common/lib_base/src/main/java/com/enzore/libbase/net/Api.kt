package com.dawn.kotlinbasedemo.http

import com.enzore.libbase.BuildConfig
import com.enzore.libbase.net.CommonService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun <T> getApi(url:String,apiClazz:Class<T>):Lazy<T>{
    return lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(getOkHttpClient())
            .build().create(apiClazz)
    }
}

private fun getOkHttpClient(): OkHttpClient {
    val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS) //设置读取超时时间
        .writeTimeout(30, TimeUnit.SECONDS) //设置写的超时时间
        .connectTimeout(30, TimeUnit.SECONDS)
    if (BuildConfig.DEBUG) {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        builder.addInterceptor(httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        })
    }
    return builder.build()
}

val commonApi by getApi("https://www.wanandroid.com/",CommonService::class.java)

