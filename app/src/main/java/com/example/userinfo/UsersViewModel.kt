package com.example.userinfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.userinfo.db.entity.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class UsersViewModel(private val repository: Repository) : ViewModel() {
    private var ldListOfUsers: MutableLiveData<List<User>>? = null
    private var compositeDisposable:CompositeDisposable = CompositeDisposable()

    fun getUsers(): LiveData<List<User>> {
        if (ldListOfUsers == null) ldListOfUsers = MutableLiveData()
        compositeDisposable.add(
            repository.getAllUserFromDb().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    ldListOfUsers!!.postValue(it)
                }, {
                    Log.e("ERROR", "getUsers: ${it.message}", )
                })
        )
        return ldListOfUsers as MutableLiveData<List<User>>
    }
}