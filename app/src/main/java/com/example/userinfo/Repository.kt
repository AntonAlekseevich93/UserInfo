package com.example.userinfo

import android.util.Log
import com.example.userinfo.api.ApiFactory
import com.example.userinfo.api.ApiService
import com.example.userinfo.db.AppDatabase
import com.example.userinfo.db.entity.User
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class Repository(private val appDatabase: AppDatabase) {
    private val apiService: ApiService = ApiFactory.apiService


    fun getAllUserFromDb(): Single<List<User>> {
        return Single.create { itDb ->
            if (appDatabase.userDao().getCountUser() > 0) {
                itDb.onSuccess(appDatabase.userDao().getAllUsers())
            } else {
                var disposable: Disposable = apiService.getUsers().subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({
                        itDb.onSuccess(it.data)
                        appDatabase.userDao().insertListUsers(it.data)
                    }, {
                        Log.e("ERROR", "getAllUserFromDb: ${it.message}" )
                    })
            }
        }

    }
}