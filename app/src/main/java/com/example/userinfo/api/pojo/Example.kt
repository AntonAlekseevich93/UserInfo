package com.example.userinfo.api.pojo

import androidx.room.ColumnInfo
import com.example.userinfo.db.entity.User

data class Example(
    @ColumnInfo(name = "page") val page: Int,
    @ColumnInfo(name = "per_page") val perPage: Int,
    @ColumnInfo(name = "total") val total: Int,
    @ColumnInfo(name = "total_pages") val totalPages: Int,
    @ColumnInfo(name = "data") val data: List<User>
)