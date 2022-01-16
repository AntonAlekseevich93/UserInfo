package com.example.userinfo.viewmodel

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.userinfo.R
import com.example.userinfo.Repository
import com.example.userinfo.db.entity.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class UsersViewModel(private val repository: Repository) : ViewModel() {
    private var ldListOfUsers: MutableLiveData<List<User>>? = null
    private var ldUser: MutableLiveData<User>? = null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    companion object {
        @JvmStatic
        @BindingAdapter("avatar")
        fun loadImage(imgView: ImageView, avatar: String) {
            Glide.with(imgView)
                .load(avatar)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(imgView)
        }
    }

    fun getUsers(): LiveData<List<User>> {
        if (ldListOfUsers == null) ldListOfUsers = MutableLiveData()
        compositeDisposable.add(
            repository.getAllUserFromDb().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    ldListOfUsers!!.postValue(it)
                }, {
                    Log.e("ERROR", "getUsers: ${it.message}")
                })
        )
        return ldListOfUsers as MutableLiveData<List<User>>
    }

    fun getUser(id: Int): LiveData<User> {
        if (ldUser == null) ldUser = MutableLiveData()
        compositeDisposable.add(repository.getUser(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                Consumer {
                    ldUser!!.value = it
                }
            ))
        return ldUser as MutableLiveData<User>
    }

    fun getDataFromApi(): LiveData<List<User>> {
        if (ldListOfUsers == null) ldListOfUsers = MutableLiveData()
        compositeDisposable.add(
            repository.getDataFromApi().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    ldListOfUsers!!.postValue(it)
                }, {
                    Log.e("ERROR", "getUsers: ${it.message}")
                })
        )
        return ldListOfUsers as MutableLiveData<List<User>>
    }

    fun deleteUser(id: Int) {
        repository.deleteUser(id)
    }

    fun saveEditedUser(user: User) {
        repository.saveEditedUser(user)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}