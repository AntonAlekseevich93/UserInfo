package com.example.userinfo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.userinfo.R
import com.example.userinfo.db.entity.User
import com.example.userinfo.ui.listeners.IListenerAdapter
import com.example.userinfo.ui.listeners.IListenerDelete
import com.example.userinfo.viewmodel.UsersViewModel

class FragmentListOfUsers : Fragment(), IListenerAdapter, IListenerDelete,
    SwipeRefreshLayout.OnRefreshListener {
    private var listOfUsers: List<User> = ArrayList()
    private lateinit var viewModel: UsersViewModel
    private lateinit var userAdapter: UsersAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    companion object {
        const val TAG_ID: String = "userID"
        const val TAG_POSITION: String = "position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        userAdapter = UsersAdapter(listOfUsers, view.context, this)
        recyclerView.adapter = userAdapter
        val liveData: LiveData<List<User>> = viewModel.getUsers()
        liveData.observe(viewLifecycleOwner, {
            userAdapter.setListUsers(it)
        })
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh)
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    override fun itemClickListener(id: Int, position: Int) {
        val fragmentUserInfo = FragmentUserInfo()
        fragmentUserInfo.arguments = setBundle(id, position)
        fragmentUserInfo.show(
            childFragmentManager,
            FragmentUserInfo.TAG_DIALOG_FRAGMENT_USER_INFO
        )
    }

    private fun setBundle(id: Int, position: Int): Bundle {
        val args = Bundle()
        args.putInt(TAG_ID, id)
        args.putInt(TAG_POSITION, position)
        return args
    }

    override fun delete(position: Int) {
        userAdapter.remove(position)
    }

    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = true
        val ld: LiveData<List<User>> = viewModel.getDataFromApi()
        ld.observe(viewLifecycleOwner, {
            userAdapter.setListUsers(it)
            swipeRefreshLayout.isRefreshing = false
        })
    }
}