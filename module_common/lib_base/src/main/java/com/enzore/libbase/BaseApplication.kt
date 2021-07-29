package com.enzore.libbase

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * 提供全局作用域内，跨页面数据通信的唯一可信源（类似单例+EventBus的作用）
 * @property appViewModelStore ViewModelStore
 */
open class BaseApplication : Application(), ViewModelStoreOwner {

    private lateinit var appViewModelStore:ViewModelStore

    override fun onCreate() {
        super.onCreate()
        appViewModelStore = ViewModelStore()
    }

    override fun getViewModelStore(): ViewModelStore {
        return appViewModelStore
    }
}