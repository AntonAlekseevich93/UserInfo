package com.example.userinfo.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.userinfo.R
import com.example.userinfo.db.entity.User
import com.example.userinfo.viewmodel.UsersViewModel

class FragmentEditUser : Fragment() {


    private lateinit var viewModel: UsersViewModel
    private lateinit var avatarUser: String
    private  var uriAvatar: Uri? = null
    private lateinit var imageViewAvatar: ImageView
    private lateinit var edtName: EditText
    private lateinit var edtLastName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var saveButton: ImageView
    private var idUser: Int = -1

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
        return inflater.inflate(R.layout.edit_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        selectedAvatar(imageViewAvatar)
        saveButtonListener()

        val liveData: LiveData<User> = viewModel.getUser(idUser)
        liveData.observe(viewLifecycleOwner, Observer {
            setData(it)
        })

    }

    private fun initView(view: View) {
        edtName = view.findViewById(R.id.editUserName)
        edtLastName = view.findViewById(R.id.editUserLastName)
        edtEmail = view.findViewById(R.id.editUserEmail)
        imageViewAvatar = view.findViewById(R.id.imgviewEditAvatar)
        saveButton = view.findViewById(R.id.imageViewSaveEditUser)
    }

    private fun selectedAvatar(imageViewAvatar: View) {
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    uriAvatar = data?.data!!
                    println(uriAvatar)
                    setImage(uriAvatar!!)
                }
            }

        imageViewAvatar.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_OPEN_DOCUMENT,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            resultLauncher.launch(intent)
        }
    }

    private fun setImage(objects: Any) {
        Glide.with(this)
            .load(objects)
            .apply(RequestOptions.circleCropTransform())
            .apply(RequestOptions.overrideOf(400, 400))
            .apply(RequestOptions().placeholder(R.mipmap.ic_launcher_round))
            .into(imageViewAvatar)
    }

    private fun saveButtonListener() {
        saveButton.setOnClickListener {

            val user = User(
                idUser, edtEmail.text.toString(), edtName.text.toString(),
                edtLastName.text.toString(), uriAvatar?.toString() ?: avatarUser
            )
            viewModel.saveEditedUser(user)
            parentFragmentManager.popBackStack()
        }
    }

    private fun setData(user: User) {
        edtName.setText(user.firstName)
        edtLastName.setText(user.lastName)
        edtEmail.setText(user.email)
        avatarUser = user.avatar
        setImage(avatarUser)
    }


}
