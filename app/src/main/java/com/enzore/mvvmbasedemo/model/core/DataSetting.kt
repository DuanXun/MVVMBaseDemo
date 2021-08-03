package com.enzore.mvvmbasedemo.model.core

import com.enzore.libutils.SharedPref
import com.enzore.tjcloud.data.ActInfo
import com.enzore.tjcloud.data.UserInfo

/**
 *Des:
 *Time:2021/7/29 - 17:02
 *Author:enzore
 */
object DataSetting {
    var phoneNum by SharedPref("phoneNum","18210904634")

    var userInfo: UserInfo? = null

    var actInfo: ActInfo? = null

    var isLogin = false
        get() = userInfo!=null
        private set
}