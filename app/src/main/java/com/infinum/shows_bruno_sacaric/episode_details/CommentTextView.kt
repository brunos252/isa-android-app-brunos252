package com.infinum.shows_bruno_sacaric.episode_details

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.infinum.shows_bruno_sacaric.R
import kotlinx.android.synthetic.main.view_comment_text.view.*

class CommentTextView  @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attributes, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_comment_text, this)

        postButton.setOnClickListener {
            postComment(postButton.text.toString())
            commentText.text = null
        }

        addCommentLayout.setOnClickListener {
            commentText.performClick()
        }

    }

    fun postComment(comment: String) {
        //TODO
    }

}

