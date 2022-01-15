package com.example.userinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.userinfo.db.AppDatabase
import com.example.userinfo.viewmodel.Factory

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db: AppDatabase? = AppDatabase.getAppDataBase(this)
        val viewModel: UsersViewModel = ViewModelProvider(this,
            Factory(db?.let { Repository(it) })).get(UsersViewModel::class.java)
//        val viewModel: UsersViewModel by viewModels { Factory(db?.let { Repository(it) }) }
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.container, FragmentListOfUsers())
                .commit()
        }

//        val disposable: Disposable = apiService.getUsers().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//
//
//            },
//                {
//
//                })

    }
}