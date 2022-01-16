package com.example.userinfo.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.userinfo.R
import com.example.userinfo.databinding.EditUserBinding
import com.example.userinfo.db.entity.User
import com.example.userinfo.viewmodel.UsersViewModel

class FragmentEditUser : Fragment() {
    private lateinit var viewModel: UsersViewModel
    private lateinit var avatarUser: String
    private var uriAvatar: Uri? = null
    private var idUser: Int = -1
    private lateinit var binding: EditUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(UsersViewModel::class.java)
        idUser = requireArguments().getInt(FragmentListOfUsers.TAG_ID)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_user, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedAvatar()
        saveButtonListener()

        val liveData: LiveData<User> = viewModel.getUser(idUser)
        liveData.observe(viewLifecycleOwner, Observer {
            avatarUser = it.avatar
            binding.user = it
        })

    }

    private fun selectedAvatar() {
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    uriAvatar = data?.data!!
                    FragmentUserInfo.loadImg(binding.imgviewEditAvatar, uriAvatar!!.toString())
                }
            }
        binding.imgviewEditAvatar.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_OPEN_DOCUMENT,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            resultLauncher.launch(intent)
        }
    }

    private fun saveButtonListener() {
        binding.imageViewSaveEditUser.setOnClickListener {
            val user = User(
                idUser,
                binding.editUserEmail.text.toString(),
                binding.editUserName.text.toString(),
                binding.editUserLastName.text.toString(),
                uriAvatar?.toString() ?: avatarUser
            )
            viewModel.saveEditedUser(user)
            parentFragmentManager.popBackStack()
        }
    }
}
