package com.infinum.shows_bruno_sacaric.ui.episodes

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import com.infinum.shows_bruno_sacaric.R
import kotlinx.android.synthetic.main.dialog_add_photo.*
import kotlinx.android.synthetic.main.fragment_add_episode.*
import kotlinx.android.synthetic.main.dialog_number_picker.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_add_photo.*
import java.io.File
import java.io.IOException

const val MY_CAMERA_PERMISSION = 707
const val MY_READ_PERMISSION = 606

const val SEASON = "SEASON"
const val EPISODE = "EPISODE"
const val MEDIA_PATH = "MEDIA_PATH"
const val DESC_MIN_LENGTH = 50
const val SEASON_MIN_VALUE = 1
const val SEASON_MAX_VALUE = 20
const val EPISODE_MIN_VALUE = 1
const val EPISODE_MAX_VALUE = 99

class AddEpisodeFragment : Fragment() {

    companion object {
        fun newInstance(index: Int) = AddEpisodeFragment().apply {
            val args = Bundle()
            args.putInt(SHOW_KEY, index)
            arguments = args
        }
    }

    var seasonNumber = 1
    var episodeNumber = 1

    var photoDialog: Dialog? = null
    var currentPhotoPath: String = ""
    var mediaFile: File? = null

    private lateinit var viewModel: EpisodesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        numberPickerText.text = "S %02d, E %02d".format(seasonNumber, episodeNumber)

        viewModel = ViewModelProviders.of(activity!!).get(EpisodesViewModel::class.java)
        toolbar.title = getString(R.string.add_episode)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_gray)
        toolbar.setNavigationOnClickListener {
            fragmentManager?.popBackStack()
        }

        if(savedInstanceState != null){
            seasonNumber = savedInstanceState.getInt(SEASON)
            episodeNumber = savedInstanceState.getInt(EPISODE)
            numberPickerText.text = "S %02d, E %02d".format(seasonNumber, episodeNumber)
            currentPhotoPath = savedInstanceState.getString(MEDIA_PATH)
            if(currentPhotoPath != ""){
                val imageUri = Uri.parse(currentPhotoPath)
                imageView.setImageURI(imageUri)
                addPhotoView.photoAdded()
                mediaFile = File(currentPhotoPath)
            }
        }

        val textWatcher = object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                saveButtonCheckState()
                descLayout.error =
                    if(descText.text.toString().length >= DESC_MIN_LENGTH || descText.text.isNullOrEmpty()) null
                    else getString(R.string.invalidDescription)

            }
        }

        titleText.addTextChangedListener(textWatcher)
        descText.addTextChangedListener(textWatcher)

        addPhotoView.setOnClickListener {
            showPhotoDialog()
        }

        numberPickerText.setOnClickListener {
            showNpDialog()
        }

        SaveButton.setOnClickListener {
            viewModel.addEpisode(mediaFile!!, titleText.text.toString(),
                descText.text.toString(), seasonNumber.toString(), episodeNumber.toString())
            fragmentManager?.popBackStack()
        }
    }

    fun saveButtonCheckState() {
        SaveButton.isEnabled = !titleText.text.isNullOrEmpty()
                && descText.text.toString().length >= DESC_MIN_LENGTH
                && currentPhotoPath != ""
    }

    private fun onCameraClick(){

        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.CAMERA)) {
                val builder = AlertDialog.Builder(requireContext())
                builder
                    .setTitle(getString(R.string.dialog_permission_title))
                    .setMessage(getString(R.string.dialog_permission_camera_message))
                    .setPositiveButton(getString(R.string.OK)){ _, _ ->
                        requestPermissions(permissions,
                            MY_CAMERA_PERMISSION
                        )
                    }.show()
            } else {
                requestPermissions(permissions,
                    MY_CAMERA_PERMISSION
                )
            }
        } else {
            openCamera()
        }
    }

    private fun onGalleryClick(){
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                val builder = AlertDialog.Builder(requireContext())
                builder
                    .setTitle(getString(R.string.dialog_permission_title))
                    .setMessage(getString(R.string.dialog_permission_gallery_message))
                    .setPositiveButton(getString(R.string.OK)){_, _ ->
                        requestPermissions(permissions,
                            MY_READ_PERMISSION
                        )
                    }.show()
            } else {
                requestPermissions(permissions,
                    MY_READ_PERMISSION
                )
            }
        } else {
            openGallery()
        }
    }

    private fun openCamera(){
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
                        it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent,
                        MY_CAMERA_PERMISSION
                    )
                }
                mediaFile = photoFile!!
            }
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        createImageFile()
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, MY_READ_PERMISSION)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        mediaFile = File(
            storageDir,
            "image.jpg"
        ).apply {
            currentPhotoPath = absolutePath
        }
        return mediaFile!!
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        photoDialog?.dismiss()
        if(requestCode == MY_CAMERA_PERMISSION && resultCode == Activity.RESULT_OK){
            val imageUri = Uri.parse(mediaFile?.absolutePath)
            imageView.setImageURI(imageUri)
            addPhotoView.photoAdded()

        } else if(requestCode == MY_READ_PERMISSION && resultCode == Activity.RESULT_OK){
            imageView.setImageURI(data?.data)
            File(getAbsolutePath(data?.data!!)).copyTo(mediaFile!!, true)
            addPhotoView.photoAdded()
        }
        saveButtonCheckState()
    }

    fun getAbsolutePath(uri: Uri) : String? {
        val filePath = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor: Cursor? = context?.contentResolver
            ?.query(uri, filePath, null, null, null)
        assert(cursor != null)
        cursor?.moveToFirst()
        val index: Int? = cursor?.getColumnIndex(filePath[0])
        val path = if(index != null) cursor.getString(index) else ""
        if(cursor != null)
            cursor.close()
        return path
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        photoDialog?.dismiss()
        when(requestCode){
            MY_CAMERA_PERMISSION -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED){
                    openCamera()
                }
            }
            MY_READ_PERMISSION -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openGallery()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SEASON, seasonNumber)
        outState.putInt(EPISODE, episodeNumber)
        if (mediaFile != null) {
            outState.putString(MEDIA_PATH, mediaFile?.absolutePath)
        }
    }

    fun showNpDialog(){
        val npDialog = Dialog(requireContext())
        npDialog.setContentView(R.layout.dialog_number_picker)

        val np1 = npDialog.numberPicker1
        val np2 = npDialog.numberPicker2

        np1.minValue = SEASON_MIN_VALUE
        np1.maxValue = SEASON_MAX_VALUE
        np1.wrapSelectorWheel = false

        np2.minValue = EPISODE_MIN_VALUE
        np2.maxValue = EPISODE_MAX_VALUE
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
        photoDialog?.setContentView(R.layout.dialog_add_photo)
        photoDialog?.show()

        photoDialog?.cameraText?.setOnClickListener {
            onCameraClick()
        }

        photoDialog?.galleryText?.setOnClickListener {
            onGalleryClick()
        }
    }
}
