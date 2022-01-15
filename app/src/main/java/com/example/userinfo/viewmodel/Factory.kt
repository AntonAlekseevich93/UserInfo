package com.example.userinfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.userinfo.Repository
import com.example.userinfo.UsersViewModel

class Factory(private val repository: Repository?) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return repository?.let { UsersViewModel(it) } as T
    }
}