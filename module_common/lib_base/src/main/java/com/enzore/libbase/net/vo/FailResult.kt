package com.enzore.libbase.net.vo

/**
 *Des: 接口异常返回
 *Time:2021/4/17 - 19:27
 *Author:enzore
 */
class FailResult<String> : ResultWrap<String>() {
    override var exception: Exception? = null
}