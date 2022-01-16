package com.example.userinfo.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.userinfo.R
import com.example.userinfo.databinding.UserItemForListBinding
import com.example.userinfo.db.entity.User
import com.example.userinfo.ui.listeners.IListenerAdapter

class UsersAdapter(
    private var listOfUsers: List<User>,
    private val context: Context,
    private val itemListener: IListenerAdapter
) :
    RecyclerView.Adapter<UsersAdapter.UsersHolder>() {


    fun setListUsers(list: List<User>) {
        listOfUsers = list
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        listOfUsers = ArrayList(listOfUsers).apply { removeAt(position) }
        notifyItemRemoved(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding: UserItemForListBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.user_item_for_list, parent, false)
        return UsersHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersHolder, position: Int) {
        holder.bind(listOfUsers[position], itemListener, position)

    }

    override fun getItemCount(): Int {
        return listOfUsers.size
    }


    class UsersHolder(
        private val binding: UserItemForListBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(user: User, listener: IListenerAdapter, position: Int) {
            binding.user = user
            binding.position = position
            binding.handler = listener

            binding.executePendingBindings()
        }




    }
}