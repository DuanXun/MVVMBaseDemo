package com.enzore.libbase.net.vo

/**
 *@Des: 数据包装接口（可根据接口返回的数据格式修改字段）
 *@Time: 2021/4/16
 *@Author: enzore
 */
open class ResultWrap<out T> {
    val data: T?=null
    open var code: Int = -1
    open var msg: String? = null
    val extra: String? = null
    val success: Boolean? = null
    open var exception: Exception?=null
}

inline fun <T> ResultWrap<out T>.success(block: ResultWrap<out T>.()->Unit): ResultWrap<out T> {
    return if (exception==null){
        block()
        this
    }else{
        this
    }
}

inline fun <T> ResultWrap<out T>.fail(block:Exception.()->Unit){
    if (exception!=null){
        block(exception!!)
    }
}