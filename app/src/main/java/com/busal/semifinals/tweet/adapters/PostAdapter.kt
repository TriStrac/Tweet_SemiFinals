package com.busal.semifinals.tweet.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busal.semifinals.tweet.TweetDetailsActivity
import com.busal.semifinals.tweet.constants.Constants
import com.busal.semifinals.tweet.databinding.ActivityPostAdapterBinding
import com.busal.semifinals.tweet.models.Post

class PostAdapter(
    private val activity: Activity,
    private val postList: List<Post>,
): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(
        private val activity: Activity,
        private val binding: ActivityPostAdapterBinding,
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.nameText.text = post.name
            binding.tweetText.text = post.description
            binding.item.setOnClickListener {
                val intent = Intent(activity, TweetDetailsActivity::class.java)
                intent.putExtra(Constants.PARAM_ID, post.id)
                activity.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityPostAdapterBinding.inflate(
            inflater,
            parent,
            false,
        )
        return PostViewHolder(activity, binding)
    }

    override fun getItemCount() = postList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postList[position])
    }
}