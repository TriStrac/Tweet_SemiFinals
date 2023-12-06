package com.busal.semifinals.tweet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.busal.semifinals.tweet.databinding.ActivityCreateTweetBinding
import com.busal.semifinals.tweet.models.Post
import com.busal.semifinals.tweet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateTweetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTweetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTweetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.saveTweetButton.setOnClickListener {
            if (binding.enterNameText.text.isNullOrBlank()) {
                Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show()
            } else if (binding.enterTweetText.text.isNullOrBlank()) {
                Toast.makeText(this, "Enter Tweet", Toast.LENGTH_SHORT).show()
            } else {
                createPost()
            }
        }
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

    private fun createPost() {
        val post = Post(
            id = "",
            name = binding.enterNameText.text.toString(),
            description = binding.enterTweetText.text.toString()
        )
        RetrofitClient.apiService.createPost(post).enqueue(object: Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    finish()
                } else {
                    response.errorBody()?.string()?.let { showError(it) }
                }
            }
            override fun onFailure(call: Call<Post>, t: Throwable) {
                t.message?.let { showError(it) }
            }
        })
    }

    private fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}