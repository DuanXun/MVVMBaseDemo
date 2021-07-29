package com.enzore.libbase.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enzore.libbase.net.vo.ResultWrap
import com.enzore.libbase.repository.BaseRepository

open class BaseViewModel : ViewModel() {

    val loadingEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    private fun showLoading(){
        loadingEvent.value = true
    }
    private fun dismissLoading(){
        loadingEvent.value = false
    }

    suspend fun <T,REP: BaseRepository> httpreqLoading(repository:REP,
                                request:suspend REP.()-> ResultWrap<T>): ResultWrap<T>
    {
        return httpreq(repository,true,request)
    }

    suspend fun <T,REP: BaseRepository> httpreq(repository:REP,loading:Boolean=false,
                                       request:suspend REP.()-> ResultWrap<T>?): ResultWrap<T>
    {
        repository.apply {
            reqStart = {
                if (loading){
                    showLoading()
                }
            }
            reqEnd = {
                dismissLoading()
            }
        }
        return request(repository)!!
    }
}