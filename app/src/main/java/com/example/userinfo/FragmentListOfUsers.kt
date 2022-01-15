package com.example.userinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userinfo.db.entity.User

class FragmentListOfUsers : Fragment() {
    private var listOfUsers: List<User> = ArrayList()
    lateinit var viewModel:UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//            viewModel by activityViewModels()
        viewModel = ViewModelProvider(requireActivity()).get(UsersViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_of_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        val userAdapter:UsersAdapter = UsersAdapter(listOfUsers, view.context)
        recyclerView.adapter = userAdapter
        val liveData:LiveData<List<User>> = viewModel.getUsers()
        liveData.observe(viewLifecycleOwner, Observer {
            userAdapter.setListUsers(it)
        })

    }
}