package com.busal.semifinals.tweet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.busal.semifinals.tweet.adapters.PostAdapter
import com.busal.semifinals.tweet.databinding.ActivityTweetListsMainBinding
import com.busal.semifinals.tweet.models.Post
import com.busal.semifinals.tweet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TweetListsMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTweetListsMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTweetListsMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.addTweetButton.setOnClickListener {
            val intent = Intent(this, CreateTweetActivity::class.java)
            startActivity(intent)
        }
        binding.refreshButton.setOnClickListener {
            getPost()
        }
    }

    override fun onResume() {
        super.onResume()
        getPost()
    }

    private fun getPost() {
        val activity = this
        RetrofitClient.apiService.getPostList().enqueue(object: Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    val data: List<Post>? = response.body()
                    if(data != null) {
                        binding.tweetList.layoutManager = LinearLayoutManager(activity)
                        binding.tweetList.adapter = PostAdapter(activity, data)
                        binding.progress.visibility = View.GONE
                    }
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                showError()
            }
        })
    }

    private fun showError() {
        Toast.makeText(this, "Failed to load data.", Toast.LENGTH_SHORT).show()
    }
}