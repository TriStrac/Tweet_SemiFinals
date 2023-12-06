package com.busal.semifinals.tweet.network

import com.busal.semifinals.tweet.models.Post
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("/tweet/busal")
    fun getPostList(): Call<List<Post>>

    @GET("/tweet/busal/{id}")
    fun getPostById(@Path("id") id: String): Call<Post>

    @POST("/tweet/busal")
    fun createPost(@Body post: Post): Call<Post>

    @DELETE("/tweet/busal/{id}")
    fun deletePostById(@Path("id") id: String): Call<Void>

    @PUT("/tweet/busal/{id}")
    fun updatePostById(@Path("id") id: String, @Body post: Post): Call<Post>

}