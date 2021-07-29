package com.enzore.libbase.repository

import com.enzore.libbase.net.exception.BaseHttpException
import com.enzore.libbase.net.exception.LocalBadException
import com.enzore.libbase.net.exception.ServerCodeBadException
import com.enzore.libbase.net.vo.FailResult
import com.enzore.libbase.net.vo.ResultWrap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *@Des: 数据仓库基类，提供网络请求的统一实现入口
 *@Time: 2021/4/16
 *@Author: enzore
 */
open class BaseRepository {

    //请求开始前执行动作
    open var reqStart: (() -> Unit) = { }
    private fun start() {
        reqStart.invoke()
    }
    //请求结束后执行动作
    open var reqEnd: (() -> Unit) = { }
    private fun end() {
        reqEnd.invoke()
    }

    /**
     * 统一处理网络请求事件，使用协程调度切换任务线程，处理异常响应
     * @param api API
     * @param request [@kotlin.ExtensionFunctionType] SuspendFunction1<API, ResultWrap<T>?>
     * @return ResultWrap<T>
     */
    protected suspend fun <T, API> req(api: API,
                             request: suspend API.() -> ResultWrap<T>?): ResultWrap<T> {
        var response: ResultWrap<T>? = null
        try {
            start()
            response = withContext(Dispatchers.IO) {
                request(api)
            }
            if (response == null) {
                throw IllegalArgumentException("连接异常，获取数据为空")
            }
            if (response.code != 1) {
                throw  ServerCodeBadException(response)
            }
        } catch (throwable: Throwable) {
            if (response == null) {
                response = FailResult()
            }
            val e = handleException(throwable)
            response.exception = e
            response.msg = e.message
            throwable.printStackTrace()
        } finally {
            end()
            return response!!
        }
    }

    /**
     * 处理异常
     * @param throwable Throwable
     * @return Exception
     */
    private fun handleException(throwable: Throwable):Exception{
        return if (throwable is BaseHttpException) {
            throwable
        } else if(throwable is CancellationException){
            CancellationException()
        }else{
            LocalBadException(throwable)
        }
    }
}