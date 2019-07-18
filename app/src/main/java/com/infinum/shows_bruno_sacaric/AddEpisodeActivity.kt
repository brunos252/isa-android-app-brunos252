package com.infinum.shows_bruno_sacaric

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.number_picker_dialog.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import android.graphics.Bitmap

class AddEpisodeActivity : AppCompatActivity(){

    val MY_CAMERA_PERMISSION = 707
    val MY_READ_PERMISSION = 606

    val SEASON = "SEASON"
    val EPISODE = "EPISODE"

    var seasonNumber = 1
    var episodeNumber = 1

    lateinit var sharedPreferences : SharedPreferences
    lateinit var sharedPreferenceEditor : SharedPreferences.Editor

    companion object {
        const val SHOW_KEY = "SHOW"

        fun newInstance(context: Context, index: Int): Intent {
            val intent = Intent(context, AddEpisodeActivity::class.java)
            intent.putExtra(SHOW_KEY, index)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        seasonNumber = sharedPreferences.getInt(SEASON, 1)
        episodeNumber = sharedPreferences.getInt(EPISODE, 1)
        numberPickerText.text = "S %02d, E %02d".format(seasonNumber, episodeNumber)

        if(savedInstanceState != null){
            seasonNumber = savedInstanceState.getInt(SEASON)
            episodeNumber = savedInstanceState.getInt(EPISODE)
            numberPickerText.text = "S %02d, E %02d".format(seasonNumber, episodeNumber)
        }

        val index = intent.getIntExtra(SHOW_KEY, 1)

        toolbar.title = "Add episode"
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            this.onBackPressed()
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

        SaveButton.setOnClickListener {
            val split = numberPickerText.text.split(',')
            val season = Integer.parseInt(split[0].substring(2))
            val episode = Integer.parseInt(split[1].substring(3))

            ShowsList.listOfShows[index].episodes.add(Episode(titleText.text.toString(), descText.text.toString(),
                season, episode))
            val returnIntent = Intent()
            returnIntent.putExtra("result", true)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    fun onClick(view: View){
        showNpDialog()
    }

    fun onCameraClick(view: View){

        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permission").setMessage("Camera is needed to take a photo, storage to keep it")
                    .setPositiveButton("OK"){_, _ ->
                        ActivityCompat.requestPermissions(this, permissions, MY_CAMERA_PERMISSION
                        )
                    }.show()
            } else {
                ActivityCompat.requestPermissions(this, permissions, MY_CAMERA_PERMISSION)

            }
        } else {
            openCamera()
        }


    }

    fun onGalleryClick(view: View){
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permission").setMessage("storage is where your photos are")
                    .setPositiveButton("OK"){_, _ ->
                        ActivityCompat.requestPermissions(this, permissions, MY_READ_PERMISSION
                        )
                    }.show()
            } else {
                ActivityCompat.requestPermissions(this, permissions, MY_READ_PERMISSION)

            }
        } else {
            openGallery()
        }
    }

    fun openCamera(){
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, MY_CAMERA_PERMISSION)
    }

    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, MY_READ_PERMISSION)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_CAMERA_PERMISSION && resultCode == Activity.RESULT_OK){
            val photo = data?.getExtras()?.get("data") as Bitmap
            imageView.setImageBitmap(photo)
            imageView.visibility = View.VISIBLE
            changePhotoText.visibility = View.VISIBLE
            uploadPhotoText.visibility = View.GONE
            cameraImage.visibility = View.GONE
        } else if(requestCode == MY_READ_PERMISSION && resultCode == Activity.RESULT_OK){
            val selectedImage = data?.getData()
            imageView.setImageURI(selectedImage)
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
    }

    fun showNpDialog(){
        val npDialog = Dialog(this)
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
        val photoDialog = Dialog(this)
        photoDialog.setContentView(R.layout.add_photo_dialog)
        photoDialog.show()
    }

    override fun onStop(){
        super.onStop()
        sharedPreferenceEditor = sharedPreferences.edit()
        sharedPreferenceEditor.putInt(SEASON, seasonNumber)
        sharedPreferenceEditor.putInt(EPISODE, episodeNumber)
        sharedPreferenceEditor.apply()
    }
}
