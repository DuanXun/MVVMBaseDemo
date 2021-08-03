package com.enzore.mvvmbasedemo.model.data

/**
 *Des:
 *Time:2021/7/29 - 17:27
 *Author:enzore
 */
data class PageEntity<T>(
    val curPage: Int,
    val datas: List<T>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)