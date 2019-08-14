package com.infinum.shows_bruno_sacaric.ui.episode_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinum.shows_bruno_sacaric.R
import com.infinum.shows_bruno_sacaric.network.models.EpisodeComment
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentsAdapter() :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private var listOfComments = listOf<EpisodeComment>()

    override fun getItemCount(): Int = listOfComments.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {

        val comment = listOfComments[position]

        with(holder.itemView){
            with(comment) {
                commentName.text = userEmail
                commentEditText.text = text
            }
        }
    }

    fun setData(comments: List<EpisodeComment>) {
        listOfComments = comments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_comment,
            parent, false))
    }

    inner class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}