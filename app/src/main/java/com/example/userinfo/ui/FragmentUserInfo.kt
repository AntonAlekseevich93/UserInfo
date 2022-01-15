package com.example.userinfo.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.userinfo.R
import com.example.userinfo.db.entity.User
import com.example.userinfo.ui.listeners.IListenerDelete
import com.example.userinfo.viewmodel.UsersViewModel


class FragmentUserInfo : DialogFragment() {
    private lateinit var viewModel: UsersViewModel
    private lateinit var iListenerDelete: IListenerDelete

    private lateinit var tvNameUser: TextView
    private lateinit var tvLastNameUser: TextView
    private lateinit var tvEmailUser: TextView
    private lateinit var imgAvatarUser: ImageView
    private lateinit var imgDeleteUser: ImageView
    private lateinit var imgEditUser: ImageView

    private var idUser: Int = -1
    private var position: Int = -1


    companion object {
        const val TAG_DIALOG_FRAGMENT_USER_INFO = "createNewDialogInfo"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (requireParentFragment() is IListenerDelete)
            iListenerDelete = requireParentFragment() as IListenerDelete
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(UsersViewModel::class.java)
        idUser = requireArguments().getInt(FragmentListOfUsers.TAG_ID)
        position = requireArguments().getInt(FragmentListOfUsers.TAG_POSITION)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View = requireActivity().layoutInflater.inflate(R.layout.info_user, null)
        tvNameUser = view.findViewById(R.id.tvInfoUserFirstName)
        tvLastNameUser = view.findViewById(R.id.tvInfoUserLastName)
        tvEmailUser = view.findViewById(R.id.tvUserInfoEmail)
        imgAvatarUser = view.findViewById(R.id.imageViewAvatarUserInfoUser)
        imgDeleteUser = view.findViewById(R.id.imageViewDeleteUser)
        imgEditUser = view.findViewById(R.id.imageViewEditUser)

        val liveData: LiveData<User> = viewModel.getUser(idUser)
        liveData.observe(this, Observer {
            setData(it)
        })

        imgDeleteUser.setOnClickListener {
            viewModel.deleteUser(idUser)
            iListenerDelete.delete(position)
            dismiss()
        }

        imgEditUser.setOnClickListener {
            val editFragment = FragmentEditUser()
            editFragment.arguments =
                setBundle(idUser)
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.container, editFragment)
                ?.addToBackStack(null)
                ?.commit()
        }



        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

    private fun setBundle(
        id: Int
    ): Bundle {
        val args = Bundle()
        args.putInt(FragmentListOfUsers.TAG_ID, id)
        return args
    }



    private fun setData(user: User) {
        tvNameUser.text = user.firstName
        tvLastNameUser.text = user.lastName
        tvEmailUser.text = user.email
        Glide.with(this)
            .load(user.avatar)
            .apply(RequestOptions.circleCropTransform())
            .apply(RequestOptions.overrideOf(400, 400))
            .apply(RequestOptions().placeholder(R.mipmap.ic_launcher_round))
            .into(imgAvatarUser)
    }

}