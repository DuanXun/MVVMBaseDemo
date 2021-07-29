package com.enzore.common.kt.util

import android.content.Context
import android.widget.Toast
import com.safframework.utils.getApplicationContext

/**
 * Toast工具类
 * @receiver Any
 * @param context Context
 * @param duration Int
 * @return Toast
 */
fun Any.toast(context: Context = getApplicationContext(), duration: Int = Toast.LENGTH_SHORT): Toast {
    return when(this){
        is Int -> Toast.makeText(context, this, duration).apply { show() }
        is String -> Toast.makeText(context, this, duration).apply { show() }
        else -> Toast.makeText(context, this.toString(), duration).apply { show() }
    }
}