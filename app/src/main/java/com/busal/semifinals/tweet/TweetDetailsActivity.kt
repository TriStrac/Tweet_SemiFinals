package com.busal.semifinals.tweet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.busal.semifinals.tweet.constants.Constants
import com.busal.semifinals.tweet.databinding.ActivityTweetDetailsBinding
import com.busal.semifinals.tweet.models.Post
import com.busal.semifinals.tweet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TweetDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTweetDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTweetDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.deleteButton.setOnClickListener {
            deleteTweet()
        }

        binding.updateButton.setOnClickListener {
            binding.updateNameText.visibility = View.VISIBLE
            binding.updateTweetText.visibility = View.VISIBLE
            binding.updateTweetButton.visibility = View.VISIBLE
            binding.cancelButton.visibility = View.VISIBLE

            binding.updateButton.visibility = View.INVISIBLE
            binding.deleteButton.visibility = View.INVISIBLE
            binding.tweetTextView.visibility = View.INVISIBLE
            binding.nameTextView.visibility = View.INVISIBLE

            binding.updateNameText.setText(binding.nameTextView.text.toString())
            binding.updateTweetText.setText(binding.tweetTextView.text.toString())
        }

        binding.cancelButton.setOnClickListener {
            binding.updateNameText.visibility = View.INVISIBLE
            binding.updateTweetText.visibility = View.INVISIBLE
            binding.updateTweetButton.visibility = View.INVISIBLE
            binding.cancelButton.visibility = View.INVISIBLE

            binding.updateButton.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.VISIBLE
            binding.tweetTextView.visibility = View.VISIBLE
            binding.nameTextView.visibility = View.VISIBLE
        }

        binding.updateTweetButton.setOnClickListener {
            val postId = intent.getStringExtra(Constants.PARAM_ID)
            if (postId != null) {
                updateTweet(postId)
            }
            finish()
            val intent = Intent(this, TweetDetailsActivity::class.java)
            intent.putExtra(Constants.PARAM_ID, postId)
            startActivity(intent)

        }

    }

    override fun onResume() {
        super.onResume()
        getPost()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateTweet(id:String) {
        val newName = binding.updateNameText.text.toString()
        val newTweet = binding.updateTweetText.text.toString()
        val updatedPost = Post(
            id = id,
            name = newName,
            description = newTweet
        )
        RetrofitClient.apiService.updatePostById(id, updatedPost).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    finish()
                } else {
                    showError("Failed to update post")
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                showError("Failed to update post")
            }
        })
    }

    private fun deleteTweet(){
        val id = intent.getStringExtra(Constants.PARAM_ID) ?: return
        RetrofitClient.apiService.deletePostById(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    finish()
                } else {
                    showError("Failed to delete post")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showError("Failed to delete post")
            }
        })
    }

    private fun getPost() {
        val id = intent.getStringExtra(Constants.PARAM_ID) ?: return
        RetrofitClient.apiService.getPostById(id).enqueue(object: Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    val data: Post? = response.body()
                    if(data != null) {
                        binding.nameTextView.text = data.name
                        binding.tweetTextView.text = data.description
                        binding.progress.visibility = View.GONE
                    }
                } else {
                    showError("Failed to get tweet")
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                showError("Failed to get tweet")
            }
        })
    }

    private fun showError(msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}