package com.example.userinfo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.userinfo.R
import com.example.userinfo.Repository
import com.example.userinfo.db.AppDatabase
import com.example.userinfo.viewmodel.Factory
import com.example.userinfo.viewmodel.UsersViewModel

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db: AppDatabase? = AppDatabase.getAppDataBase(this)
        val viewModel: UsersViewModel = ViewModelProvider(
            this,
            Factory(db?.let { Repository(it) })
        ).get(UsersViewModel::class.java)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, FragmentListOfUsers())
                .commit()
        }
    }
}