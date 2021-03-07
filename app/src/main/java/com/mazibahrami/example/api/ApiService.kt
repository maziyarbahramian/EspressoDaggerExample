package com.mazibahrami.example.api

import com.mazibahrami.example.models.BlogPost
import com.mazibahrami.example.models.Category
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("blogs")
    suspend fun getBlogPosts(
        @Query("category") category: String
    ): List<BlogPost>

    @GET("blogs")
    suspend fun getAllBlogPosts(): List<BlogPost>

    @GET("categories")
    suspend fun getCategories(): List<Category>

    companion object {
        const val BASE_URL = "https://open-api.xyz/placeholder/"
    }
}