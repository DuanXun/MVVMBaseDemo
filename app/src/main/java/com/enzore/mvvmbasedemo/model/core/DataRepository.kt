package com.enzore.mvvmbasedemo.model.core

import com.enzore.libbase.net.vo.ResultWrap
import com.enzore.libbase.repository.BaseRepository
import com.enzore.mvvmbasedemo.model.core.net.baseApi
import com.enzore.mvvmbasedemo.model.data.BannerInfo
import com.enzore.mvvmbasedemo.model.data.FriendInfo
import com.enzore.mvvmbasedemo.model.data.IndexItem
import com.enzore.mvvmbasedemo.model.data.PageEntity
import com.enzore.tjcloud.data.UserInfo

/**
 *Des:
 *Time:2021/7/29 - 17:05
 *Author:enzore
 */
class DataRepository :BaseRepository() {

    companion object{
        fun get()= DataRepository()
    }

    suspend fun indexList(pageNum:Int):ResultWrap<PageEntity<IndexItem>>{
        return req(baseApi){
            indexList(pageNum)
        }
    }

    suspend fun indexBanners():ResultWrap<List<BannerInfo>>{
        return req(baseApi){
            indexBanner()
        }
    }

    suspend fun friend():ResultWrap<List<FriendInfo>>{
        return req(baseApi){
            friend()
        }
    }
    suspend fun login(params:MutableMap<String,String>):ResultWrap<UserInfo>{
        return req(baseApi){
            login(params)
        }
    }

    suspend fun register(params:MutableMap<String,String>):ResultWrap<Any>{
        return req(baseApi){
            register(params)
        }
    }

}