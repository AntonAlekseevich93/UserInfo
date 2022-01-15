package com.example.userinfo.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.userinfo.R
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
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.user_item_for_list, parent, false)
        return UsersHolder(view, itemListener, this)
    }

    override fun onBindViewHolder(holder: UsersHolder, position: Int) {
        holder.tvName.text = listOfUsers[position].firstName
        holder.tvLastName.text = listOfUsers[position].lastName
        Glide.with(context)
            .load(listOfUsers[position].avatar)
            .apply(RequestOptions.circleCropTransform())
            .apply(RequestOptions().placeholder(R.mipmap.ic_launcher_round))
            .into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return listOfUsers.size
    }


    class UsersHolder(itemView: View, itemListener: IListenerAdapter, adapter: UsersAdapter) :
        RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvFirstName)
        val tvLastName: TextView = itemView.findViewById(R.id.tvLastName)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val cardViewItemUser: CardView = itemView.findViewById(R.id.cardViewItemUser)

        init {
            cardViewItemUser.setOnClickListener {
                itemListener.itemClickListener(
                    adapter.listOfUsers[layoutPosition].id, layoutPosition
                )
            }
        }
    }
}