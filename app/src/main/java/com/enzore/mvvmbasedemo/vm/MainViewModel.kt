package com.enzore.mvvmbasedemo.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.enzore.common.kt.util.toast
import com.enzore.libbase.net.vo.fail
import com.enzore.libbase.net.vo.success
import com.enzore.libbase.vm.BaseViewModel
import com.enzore.libutils.SharedPref
import com.enzore.mvvmbasedemo.model.core.DataRepository
import com.enzore.mvvmbasedemo.model.data.BannerInfo
import com.enzore.mvvmbasedemo.model.data.FriendInfo
import com.enzore.mvvmbasedemo.model.data.IndexItem
import com.enzore.tjcloud.data.UserInfo
import com.google.gson.Gson
import kotlinx.coroutines.launch

/**
 *Des:
 *Time:2021/7/29 - 17:40
 *Author:enzore
 */
class MainViewModel :BaseViewModel(){

    var indexList:MutableLiveData<MutableList<IndexItem>> = MutableLiveData(mutableListOf())
    var indexBanner:MutableLiveData<List<BannerInfo>> = MutableLiveData()
    var friends:MutableLiveData<List<FriendInfo>> = MutableLiveData()
    var user:MutableLiveData<UserInfo> = MutableLiveData()
    var info:MutableLiveData<String> = MutableLiveData()

    /*var username:MutableLiveData<String> = MutableLiveData()
    var pwd:MutableLiveData<String> = MutableLiveData()*/
    var username by SharedPref("username","duanxun0")
    var pwd by SharedPref("password","dx@198610")

    var downloadPercent:MutableLiveData<Int> = MutableLiveData(0)

    private val repository by lazy {
        DataRepository.get()
    }

    fun getIndexList(pageNum:Int){
        viewModelScope.launch {
            httpreqLoading(repository){
                indexList(pageNum)
            }.success {
                val pageData = this.data
                val list = pageData!!.datas
                indexList.value!!.addAll(list)
                info.value = Gson().toJson(this.data)
            }.fail {
                "首页列表请求失败".toast()
                info.value = "首页列表请求失败"
            }
        }
    }

    fun getIndexBanners(){
        viewModelScope.launch {
            httpreqLoading(repository){
                indexBanners()
            }.success {
                indexBanner.value = this.data
                info.value = Gson().toJson(this.data)
            }.fail {
                "首页banner请求失败".toast()
                info.value = "首页banner请求失败"
            }
        }
    }

    fun getFriend(){
        viewModelScope.launch {
            httpreqLoading(repository){
                friend()
            }.success {
                friends.value = this.data
                info.value = Gson().toJson(this.data)
            }.fail {
                "常用网站请求失败".toast()
                info.value = "常用网站请求失败"
            }
        }
    }

    fun gotoLogin(){
        var params = mutableMapOf<String,String>("username" to username,"password" to pwd)
        viewModelScope.launch {
            httpreqLoading(repository){
                login(params)
            }.success {
                user.value = this.data
                info.value = Gson().toJson(this.data)
            }.fail {
                "登录失败".toast()
                info.value = "登录失败"
            }
        }
    }

    fun gotoRegister(){
        var params = mutableMapOf<String,String>("username" to "EnZore","password" to "dx@198610","repassword" to "dx@198610")
        viewModelScope.launch {
            httpreqLoading(repository){
                register(params)
            }.success {
                //friends.value = this.data
                info.value = Gson().toJson(this.data)
            }.fail {
                "注册失败".toast()
                info.value = "注册失败"
            }
        }
    }

    fun downloadFile(path:String){
        viewModelScope.launch {
            repository.download("http://t.xiazai163.com//down/kgyltxt_itmop.com.zip",
                path){
//                downloadPercent.value = it
                info.value = it.toString()
            }
        }
    }
}