package com.example.userinfo.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.userinfo.R
import com.example.userinfo.databinding.InfoUserBinding
import com.example.userinfo.db.entity.User
import com.example.userinfo.ui.listeners.IListenerDelete
import com.example.userinfo.viewmodel.UsersViewModel


class FragmentUserInfo : DialogFragment() {
    private lateinit var viewModel: UsersViewModel
    private lateinit var iListenerDelete: IListenerDelete
    lateinit var binding: InfoUserBinding

    private var idUser: Int = -1
    private var position: Int = -1


    companion object {
        const val TAG_DIALOG_FRAGMENT_USER_INFO = "createNewDialogInfo"


            @JvmStatic
            @BindingAdapter("avatarInfo")
            fun loadImg(imageViewAvatarUserInfoUser: ImageView, avatar1: String?) {

                Glide.with(imageViewAvatarUserInfoUser)
                    .load(avatar1)
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.overrideOf(400, 400))
                    .apply(RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                    .into(imageViewAvatarUserInfoUser)

            }

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

        binding =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireActivity()),
                R.layout.info_user,
                null,
                false
            )


        val liveData: LiveData<User> = viewModel.getUser(idUser)
        liveData.observe(this, Observer {
            setData(it)
        })
//
        binding.position = position
        binding.idUser = idUser
        binding.handlerDelete = iListenerDelete

        binding.imageViewEditUser.setOnClickListener {
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
            .setView(binding.root)
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
        binding.user = user

    }

}