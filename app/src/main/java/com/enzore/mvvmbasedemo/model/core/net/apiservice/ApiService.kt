package com.enzore.mvvmbasedemo.model.core.net.apiservice

import com.enzore.libbase.net.vo.ResultWrap
import com.enzore.mvvmbasedemo.model.data.BannerInfo
import com.enzore.mvvmbasedemo.model.data.FriendInfo
import com.enzore.mvvmbasedemo.model.data.IndexItem
import com.enzore.mvvmbasedemo.model.data.PageEntity
import com.enzore.tjcloud.data.UserInfo
import okhttp3.RequestBody
import retrofit2.http.*

/**
 *Des:
 *Time:2021/7/29 - 17:10
 *Author:enzore
 */
interface ApiService {

    @GET("article/list/{page}/json")
    suspend fun indexList(@Path("page") page:Int):ResultWrap<PageEntity<IndexItem>>

    @GET("banner/json")
    suspend fun indexBanner():ResultWrap<List<BannerInfo>>

    @GET("friend/json")
    suspend fun friend():ResultWrap<List<FriendInfo>>

    @POST("user/login")//username,password
    @FormUrlEncoded
    suspend fun login(@FieldMap map:Map<String,String>):ResultWrap<UserInfo>

    @POST("user/register")//username,password,repassword
    @FormUrlEncoded
    suspend fun register(@FieldMap map:Map<String,String>):ResultWrap<Any>


}