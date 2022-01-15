package com.example.userinfo.api

import com.example.userinfo.api.pojo.Example
import io.reactivex.Single
import retrofit2.http.GET

interface ApiService {
    @GET("users?page=2")
    fun getUsers(): Single<Example>
}