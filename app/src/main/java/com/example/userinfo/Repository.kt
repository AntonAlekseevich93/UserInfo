package com.example.userinfo

import android.util.Log
import com.example.userinfo.api.ApiFactory
import com.example.userinfo.api.ApiService
import com.example.userinfo.db.AppDatabase
import com.example.userinfo.db.entity.User
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Repository(private val appDatabase: AppDatabase) {
    private val apiService: ApiService = ApiFactory.apiService
    private val executorService: ExecutorService = Executors.newFixedThreadPool(2)

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
                        Log.e("ERROR", "getAllUserFromDb: ${it.message}")
                    })
            }
        }
    }

    fun deleteUser(id: Int) {
        executorService.submit(Runnable {
            appDatabase.userDao().deleteUser(id)
        })


    }

    fun saveEditedUser(user: User) {
        executorService.submit(Runnable {
            appDatabase.userDao().insert(user)
        })

    }

    fun getUser(id: Int): Single<User> {
        return Single.create {
            it.onSuccess(appDatabase.userDao().getUser(id))
        }
    }

    fun getDataFromApi():Single<List<User>>{
        return Single.create {itDb ->
            var disposable: Disposable = apiService.getUsers().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    appDatabase.userDao().insertListUsers(it.data)
                    itDb.onSuccess(appDatabase.userDao().getAllUsers())
                }, {
                    Log.e("ERROR", "getAllUserFromDb: ${it.message}")
                })
        }
    }

}