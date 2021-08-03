package com.enzore.mvvmbasedemo.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.enzore.libbase.ui.BaseActivity
import com.enzore.mvvmbasedemo.R
import com.enzore.mvvmbasedemo.databinding.ActivityMainBinding
import com.enzore.mvvmbasedemo.vm.MainViewModel

class MainActivity : BaseActivity() {

    private val mainVm by getActivityViewModel(MainViewModel::class.java)
    lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding(R.layout.activity_main)
        binding.maindata = mainVm
        binding.indexBtn.setOnClickListener {
            mainVm.getIndexList(0)
        }
        binding.bannersBtn.setOnClickListener {
            mainVm.getIndexBanners()
        }
        binding.friendsBtn.setOnClickListener {
            mainVm.getFriend()
        }
        binding.loginBtn.setOnClickListener {
            mainVm.gotoLogin()
        }
        binding.registerBtn.setOnClickListener {
            mainVm.gotoRegister()
        }
        binding.downloadBtn.setOnClickListener {
            mainVm.downloadFile("${getExternalFilesDir(null)?.path}/download/kgyltxt_itmop.zip")
        }

    }
}