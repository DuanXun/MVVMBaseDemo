package com.enzore.libbase.net

import com.enzore.libbase.net.vo.ResultWrap
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 *Des:提供上传和下载基础服务入口，用于底层统一封装
 *Time:2021/8/2 - 14:47
 *Author:enzore
 */
interface CommonService {

    @Multipart
    @POST
    suspend fun postFile(@Url url:String, @PartMap params:Map<String, RequestBody>): ResultWrap<Any>

    @Streaming
    @GET
    fun download(@Url url:String): Call<ResponseBody>
}