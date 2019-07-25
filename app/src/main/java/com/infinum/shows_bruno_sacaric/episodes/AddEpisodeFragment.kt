package com.infinum.shows_bruno_sacaric.episodes


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import com.infinum.shows_bruno_sacaric.repository.Episode
import com.infinum.shows_bruno_sacaric.R
import kotlinx.android.synthetic.main.add_photo_dialog.*
import kotlinx.android.synthetic.main.fragment_add_episode.*
import kotlinx.android.synthetic.main.number_picker_dialog.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddEpisodeFragment : Fragment() {

    companion object {
        fun newInstance(index: Int) = AddEpisodeFragment().apply {
            val args = Bundle()
            args.putInt(SHOW_KEY, index)
            arguments = args
        }
    }

    val MY_CAMERA_PERMISSION = 707
    val MY_READ_PERMISSION = 606

    val SEASON = "SEASON"
    val EPISODE = "EPISODE"
    val PHOTO_PATH = "PHOTO_PATH"
    val TITLE_TEXT = "TITLE_TEXT"
    val DESC_TEXT = "DESC_TEXT"
    val INDEX = "INDEX"

    var seasonNumber = 1
    var episodeNumber = 1

    var photoDialog: Dialog? = null
    var currentPhotoPath: String = ""

    private lateinit var viewModel: EpisodesViewModel
    private lateinit var bundleViewModel: AddEpisodeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        numberPickerText.text = "S %02d, E %02d".format(seasonNumber, episodeNumber)

        val index = arguments?.getInt(SHOW_KEY, 1)
        viewModel = ViewModelProviders.of(activity!!).get(EpisodesViewModel::class.java)
        viewModel.selectShow(index!!)
        toolbar.title = "Add episode"
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            bundleViewModel.disposeOfBundle()
            fragmentManager?.popBackStack()
        }

        bundleViewModel = ViewModelProviders.of(activity!!).get(AddEpisodeViewModel::class.java)

        if(bundleViewModel.liveData.value != null && bundleViewModel.liveData.value!!.getInt(INDEX) == index){
            val bundle : Bundle = bundleViewModel.liveData.value!!
            seasonNumber = bundle.getInt(SEASON)
            episodeNumber = bundle.getInt(EPISODE)
            titleText.setText(bundle.getString(TITLE_TEXT))
            descText.setText(bundle.getString(DESC_TEXT))
            numberPickerText.text = "S %02d, E %02d".format(seasonNumber, episodeNumber)
            currentPhotoPath = bundle.getString(PHOTO_PATH)
            currentPhotoPath
            if(currentPhotoPath != ""){
                val imageUri = Uri.parse(currentPhotoPath)
                imageView.setImageURI(imageUri)
                imageView.visibility = View.VISIBLE
                changePhotoText.visibility = View.VISIBLE
                uploadPhotoText.visibility = View.GONE
                cameraImage.visibility = View.GONE
            }
        }

        titleText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                SaveButton.isEnabled = !s.isNullOrEmpty()
            }
        })

        addPhotoLayout.setOnClickListener {
            showPhotoDialog()
        }

        numberPickerText.setOnClickListener {
            showNpDialog()
        }

        SaveButton.setOnClickListener {
            viewModel.addEpisode(
                Episode(
                    titleText.text.toString(), descText.text.toString(), seasonNumber, episodeNumber
                )
            )
            bundleViewModel.disposeOfBundle()
            fragmentManager?.popBackStack()
        }
    }

    fun onCameraClick(){

        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.CAMERA)) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Permission").setMessage("Camera is needed to take a photo, storage to keep it")
                    .setPositiveButton("OK"){_, _ ->
                        ActivityCompat.requestPermissions(activity!!, permissions, MY_CAMERA_PERMISSION
                        )
                    }.show()
            } else {
                ActivityCompat.requestPermissions(activity!!, permissions, MY_CAMERA_PERMISSION)

            }
        } else {
            openCamera()
        }
    }

    fun onGalleryClick(){
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Permission").setMessage("storage is where your photos are")
                    .setPositiveButton("OK"){_, _ ->
                        ActivityCompat.requestPermissions(requireActivity(), permissions, MY_READ_PERMISSION
                        )
                    }.show()
            } else {
                ActivityCompat.requestPermissions(requireActivity(), permissions, MY_READ_PERMISSION)

            }
        } else {
            openGallery()

        }
    }

    fun openCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch(ex: IOException){
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.sajo.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, MY_CAMERA_PERMISSION)
                    photoDialog?.dismiss()
                }
            }
        }
    }

    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, MY_READ_PERMISSION)
        photoDialog?.dismiss()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_CAMERA_PERMISSION && resultCode == Activity.RESULT_OK){
            val imageUri = Uri.parse(currentPhotoPath)
            imageView.setImageURI(imageUri)

            imageView.visibility = View.VISIBLE
            changePhotoText.visibility = View.VISIBLE
            uploadPhotoText.visibility = View.GONE
            cameraImage.visibility = View.GONE

        } else if(requestCode == MY_READ_PERMISSION && resultCode == Activity.RESULT_OK){
            currentPhotoPath = data?.getData().toString()
            imageView.setImageURI(data?.getData())
            imageView.visibility = View.VISIBLE
            changePhotoText.visibility = View.VISIBLE
            uploadPhotoText.visibility = View.GONE
            cameraImage.visibility = View.GONE
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            MY_CAMERA_PERMISSION -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED){
                    openCamera()
                } else{
                    photoDialog?.dismiss()
                }
            }
            MY_READ_PERMISSION -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openGallery()
                } else{
                    photoDialog?.dismiss()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val bundle = Bundle()
        bundle.putInt(SEASON, seasonNumber)
        bundle.putInt(EPISODE, episodeNumber)
        bundle.putString(PHOTO_PATH, currentPhotoPath)
        bundle.putString(TITLE_TEXT, titleText.text.toString())
        bundle.putString(DESC_TEXT, descText.text.toString())
        bundle.putInt(INDEX, arguments?.getInt(SHOW_KEY, 1)!!)
        bundleViewModel.addBundle(bundle)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        Log.d("on", "destroy");
        super.onDestroyView()
    }

    override fun onDetach() {
        Log.d("on", "detach")
        super.onDetach()
    }

    fun showNpDialog(){
        val npDialog = Dialog(requireContext())
        npDialog.setContentView(R.layout.number_picker_dialog)

        val np1 = npDialog.numberPicker1
        val np2 = npDialog.numberPicker2

        np1.minValue = 1
        np1.maxValue = 20
        np1.wrapSelectorWheel = false

        np2.minValue = 1
        np2.maxValue = 99
        np2.wrapSelectorWheel = false

        np1.value = seasonNumber
        np2.value = episodeNumber

        npDialog.numberPickerSaveButton.setOnClickListener {
            seasonNumber = np1.value
            episodeNumber = np2.value
            numberPickerText.text = "S %02d, E %02d".format(seasonNumber, episodeNumber)
            npDialog.dismiss()
        }
        npDialog.show()

    }

    fun showPhotoDialog(){
        photoDialog = Dialog(requireContext())
        photoDialog?.setContentView(R.layout.add_photo_dialog)
        photoDialog?.show()


        photoDialog?.cameraText?.setOnClickListener {
            onCameraClick()
        }

        photoDialog?.galleryText?.setOnClickListener {
            onGalleryClick()
        }
    }
}
