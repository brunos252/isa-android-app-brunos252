package com.infinum.shows_bruno_sacaric.ui.episode_details

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.infinum.shows_bruno_sacaric.R

class CommentTextView  @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attributes, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_comment_text, this)
    }
}

