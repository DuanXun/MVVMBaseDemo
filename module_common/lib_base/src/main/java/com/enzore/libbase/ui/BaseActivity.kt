package com.enzore.libbase.ui

import android.app.Activity
import android.app.Application
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enzore.libbase.BaseApplication
import com.enzore.libbase.vm.BaseViewModel
import com.enzore.libutils.BarUtils

/**
 *@Des: 处理页面的逻辑UI事件，提供DataBinding和ViewModel获取的统一入口
 *@Time: 2021/4/16
 *@Author: enzore
 */
open class BaseActivity : AppCompatActivity() {
    var loadingDialog: ProgressDialog?=null;
    private fun showLoading() {
        if(loadingDialog==null){
            loadingDialog=ProgressDialog(this).apply {
                setMessage("加载中。。。")
            }
        }
        if (!loadingDialog!!.isShowing) {
            loadingDialog!!.show()
        }
    }
    private fun dismissLoading() {
        loadingDialog?.takeIf { it.isShowing }?.dismiss()
        loadingDialog = null
    }

    /**
     * 获取DataBinding
     * @param layoutId Int
     * @return VBD
     */
    protected fun <VBD : ViewDataBinding> getViewDataBinding(layoutId: Int):VBD{
        val vbd:VBD = DataBindingUtil.setContentView(this, layoutId)
        vbd.lifecycleOwner = this
        return vbd
    }


    private val activityProvider: ViewModelProvider by lazy {
        ViewModelProvider(this)
    }
    private val applicationProvider: ViewModelProvider by lazy {
        ViewModelProvider(this.application as BaseApplication,getAppFactory(this))
    }
    /**
     * 获取当前activity页面ViewModel
     * @param viewModelClass Class<VM>
     * @return Lazy<VM>
     */
    protected fun <VM : BaseViewModel>  getActivityViewModel(viewModelClass: Class<VM>):Lazy<VM>{
        return lazy {
            activityProvider.get(viewModelClass).apply {
                this.loadingEvent.observe(this@BaseActivity){
                    if (it){
                        showLoading()
                    }else{
                        dismissLoading()
                    }
                }
            }
        }
    }
    /**
     * 获取全局的ViewModel
     * @param viewModelClass Class<VM>
     * @return Lazy<VM>
     */
    protected fun <VM : ViewModel>  getAppViewModel(viewModelClass: Class<VM>):Lazy<VM>{
        return lazy {
            applicationProvider.get(viewModelClass)
        }
    }
    private fun getAppFactory(activity: Activity): ViewModelProvider.Factory {
        val application: Application? = checkApplication(activity)
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application!!)
    }

    private fun checkApplication(activity: Activity): Application? {
        return activity.application
                ?: throw IllegalStateException("Your activity/fragment is not yet attached to "
                        + "Application. You can't request ViewModel before onCreate call.")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
        BarUtils.setStatusBarLightMode(this, true)
        super.onCreate(savedInstanceState)
        //根据需求决定是否注册网络监听
        // lifecycle.addObserver(NetWordStateManager.get())
    }

    fun <T : Activity> startActivity(clazz: Class<T>, bundle: Bundle? = null, requestCode: Int = -1){
        val intent = Intent(this, clazz)
        bundle?.let {
            intent.putExtra("data", it)
        }
        startActivityForResult(intent, requestCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }
}