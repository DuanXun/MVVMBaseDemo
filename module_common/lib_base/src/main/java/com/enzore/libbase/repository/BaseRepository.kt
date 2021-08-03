package com.enzore.libbase.repository

import android.os.Environment
import android.util.Log
import com.dawn.kotlinbasedemo.http.commonApi
import com.enzore.libbase.net.exception.BaseHttpException
import com.enzore.libbase.net.exception.LocalBadException
import com.enzore.libbase.net.exception.ServerCodeBadException
import com.enzore.libbase.net.vo.FailResult
import com.enzore.libbase.net.vo.ResultWrap
import com.safframework.utils.createOrExistsDir
import com.safframework.utils.createOrExistsFile
import com.safframework.utils.isConnected
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream
import java.io.OutputStream

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
            if (!isConnected()){
                throw IllegalArgumentException("网络异常，请检查网络连接情况")
            }
            response = withContext(Dispatchers.IO) {
                request(api)
            }
            if (response == null) {
                throw IllegalArgumentException("连接异常，获取数据为空")
            }
            if (response.errorCode != 0) {
                throw  ServerCodeBadException(response)
            }
        } catch (throwable: Throwable) {
            if (response == null) {
                response = FailResult()
            }
            val e = handleException(throwable)
            response.exception = e
            response.errorMsg = e.message
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

    protected suspend fun postFile(url:String,file: File): ResultWrap<Any>{
        var params:Map<String, RequestBody> = mutableMapOf()
        //初始化params
        var response: ResultWrap<Any>? = req(commonApi){
            postFile(url,params)
        }
        return response!!
    }

    suspend fun download(url:String,
                                   filePath: String ,
                                   process:(Int)->Unit): Boolean{
        val file = File(filePath)
        if (createOrExistsFile(file)){
            return download(url,file,process)
        }
        Log.e("MM","下载文件创建失败")
        return false
    }

    suspend fun download(url:String,file: File,process:(Int)->Unit): Boolean{
        withContext(Dispatchers.IO){
            if(!file.exists()){
                file.createNewFile()
            }
            val response = commonApi.download(url).execute()
            val body = response.body()
            if (response.isSuccessful && body!=null){
                var inStream:InputStream?=null
                var outStream:OutputStream?=null
                try {
                    inStream = body.byteStream()
                    outStream = file.outputStream()
                    val contentLenght = body.contentLength()
                    var currentLenght = 0L
                    val buff = ByteArray(1024)
                    var len = inStream.read(buff)
                    var percent = 0
                    while (len != -1) {
                        outStream.write(buff, 0, len)
                        currentLenght += len
                        if ((currentLenght * 100 / contentLenght).toInt() > percent) {
                            percent = (currentLenght * 100 / contentLenght).toInt()
                            Log.w("MM",".................$percent")
                            //TODO UI线程更新
                            withContext(Dispatchers.Main){
                                process.invoke(percent)
                            }
                        }
                        len = inStream.read(buff)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    inStream?.close()
                    outStream?.close()
                }
            }
        }
        return true
    }


}