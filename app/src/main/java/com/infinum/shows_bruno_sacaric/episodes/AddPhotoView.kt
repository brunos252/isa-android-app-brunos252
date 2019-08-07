package com.infinum.shows_bruno_sacaric.episodes

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.infinum.shows_bruno_sacaric.R
import kotlinx.android.synthetic.main.view_add_photo.view.*

class AddPhotoView  @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attributes, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_add_photo, this)
    }

    fun photoAdded() {
        imageView.visibility = View.VISIBLE
        changePhotoText.visibility = View.VISIBLE
        uploadPhotoText.visibility = View.GONE
        cameraImage.visibility = View.GONE
    }

}
