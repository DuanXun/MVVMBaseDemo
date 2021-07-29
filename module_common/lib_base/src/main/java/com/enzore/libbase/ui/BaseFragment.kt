package com.enzore.libbase.ui

import android.app.Activity
import android.app.Application
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enzore.libbase.BaseApplication
import com.enzore.libbase.vm.BaseViewModel

/**
 *Des:
 *Time:2021/5/2 - 21:07
 *Author:enzore
 */
class BaseFragment : Fragment() {

    var loadingDialog: ProgressDialog?=null;
    private fun showLoading() {
        if(loadingDialog==null){
            loadingDialog= ProgressDialog(context).apply {
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
    protected fun <VBD : ViewDataBinding> getViewDataBinding(inflater: LayoutInflater, container: ViewGroup, layoutId: Int):VBD{
        val vbd:VBD = DataBindingUtil.inflate(inflater, layoutId,container,false)
        vbd.lifecycleOwner = this
        return vbd
    }


    private val fragmentProvider: ViewModelProvider by lazy {
        ViewModelProvider(this)
    }
    private val activityProvider: ViewModelProvider by lazy {
        ViewModelProvider(requireActivity())
    }
    private val applicationProvider: ViewModelProvider by lazy {
        ViewModelProvider(requireActivity().application as BaseApplication,getAppFactory(requireActivity()))
    }
    /**
     * 获取当前fragment页面ViewModel
     * @param viewModelClass Class<VM>
     * @return Lazy<VM>
     */
    protected fun <VM : BaseViewModel>  getFragmentViewModel(viewModelClass: Class<VM>):Lazy<VM>{
        return lazy {
            fragmentProvider.get(viewModelClass).apply {
                this.loadingEvent.observe(this@BaseFragment){
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
     * 获取当前activity页面ViewModel
     * @param viewModelClass Class<VM>
     * @return Lazy<VM>
     */
    protected fun <VM : BaseViewModel>  getActivityViewModel(viewModelClass: Class<VM>):Lazy<VM>{
        return lazy {
            activityProvider.get(viewModelClass)
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

    fun <T : Activity> startActivity(clazz: Class<T>, bundle: Bundle? = null, requestCode: Int = -1){
        val intent = Intent(activity, clazz)
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