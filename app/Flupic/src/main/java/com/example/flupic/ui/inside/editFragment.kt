package com.example.flupic.ui.inside


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.flupic.databinding.FragmentEditBinding
import com.example.flupic.viewmodels.EditViewModel
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.content.Intent
import android.app.Activity
import android.app.ProgressDialog.show
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import android.net.Uri
import android.util.Log
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.example.flupic.R
import com.example.flupic.TAG
import com.example.flupic.di.ViewModelProviderFactory
import com.example.flupic.domain.FireUser
import com.example.flupic.ui.dialogue.BIS_DIA_LOC_ID
import com.example.flupic.ui.dialogue.businessLocationDialogueFragment
import dagger.android.support.DaggerFragment
import javax.inject.Inject


const val CODE_PHOTO_REQUES = 1015
const val GET_PERMISSION_REQUES = 1510


class editFragment : DaggerFragment() , businessLocationDialogueFragment.LocationListener {

    lateinit var binding: FragmentEditBinding

    //Injection of ViewModel
    @Inject
    lateinit var injectFactory: ViewModelProviderFactory

    private val editViewModel : EditViewModel by lazy {
        ViewModelProviders.of(this, injectFactory).get(
            EditViewModel::class.java)
    }

    private var newPhotoUri: Uri? = null

    private val locationDialogue = businessLocationDialogueFragment()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener { navBack() }

        binding.applyButton.setOnClickListener { applyChanges() }

        binding.materialButton.setOnClickListener { loadAndChangePhoto() }

        binding.setBusinessLocDiaButton.setOnClickListener { locationDialogue.show(childFragmentManager, BIS_DIA_LOC_ID) }

        initSpinner()

        editViewModel.curUser.observe(this, Observer {
            binding.changeableUser = it
            selectSpinner(it)
        })

        editViewModel.isDone.observe(this, Observer { if(it == true){ navBack() }})
        editViewModel.isSetLocDone.observe(this, Observer { if(it == true){ locationDialogue.onDone() } })

        return binding.root
    }

    private fun applyChanges(){

        //Block until done
        binding.shimmer.startShimmer()

        binding.fullNameInput.isEnabled = false
        binding.bioInput.isEnabled = false
        binding.numInput.isEnabled = false
        binding.spinner.isEnabled = false
        binding.backButton.isEnabled = false
        binding.materialButton.isEnabled = false
        binding.applyButton.isEnabled = false

        val fullname = binding.fullNameInput.text.toString()
        val bio = binding.bioInput.text.toString()
        val phone_num = binding.numInput.text.toString()
        val category = binding.spinner.selectedItem.toString()

        if(!fullname.isBlank()){
            editViewModel.applyChanges(fullname, bio, phone_num, category, newPhotoUri)
        }else{
            Toast.makeText(context, context!!.resources.getString(com.example.flupic.R.string.a_error_7), Toast.LENGTH_SHORT).show()
        }
    }

    fun loadAndChangePhoto(){

        if (ContextCompat.checkSelfPermission(context!!, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(android.Manifest.permission::READ_EXTERNAL_STORAGE.get())){
                requestPermissions(arrayOf(android.Manifest.permission::READ_EXTERNAL_STORAGE.get()),
                    GET_PERMISSION_REQUES
                )
            }
            requestPermissions(arrayOf(android.Manifest.permission::READ_EXTERNAL_STORAGE.get()),
                GET_PERMISSION_REQUES
            )
        }else{
            startGallery()
        }
    }

    fun startGallery(){

        val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/jpg"
        }

        if (cameraIntent.resolveActivity(activity!!.packageManager) != null) {
            startActivityForResult(cameraIntent, CODE_PHOTO_REQUES)
        }
    }

    private fun initSpinner(){

        val spinnerList: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(context!!, R.array.category_list, android.R.layout.simple_spinner_item)
            .apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

        binding.spinner.adapter = spinnerList
    }

    private fun selectSpinner(user: FireUser){
        val selectArray = resources.getStringArray(R.array.category_list)

        when(user.category){
            selectArray[0] -> binding.spinner.setSelection(0)
            selectArray[1] -> binding.spinner.setSelection(1)
            selectArray[2] -> binding.spinner.setSelection(2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == CODE_PHOTO_REQUES) {
            data?.let {
                val returnUri: Uri? = data.data

                returnUri?.let {

                    Glide.with(this)
                        .load(returnUri)
                        .into(binding.profileImage)

                    newPhotoUri = returnUri
                }
            }
        }
    }

    override fun onSetLocation() {
        editViewModel.putBusinessLocation()
    }

    fun navBack() = findNavController().popBackStack()
}
