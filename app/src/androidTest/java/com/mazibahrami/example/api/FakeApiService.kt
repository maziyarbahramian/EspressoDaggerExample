package com.mazibahrami.example.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mazibahrami.example.models.BlogPost
import com.mazibahrami.example.models.Category
import com.mazibahrami.example.util.Constants
import com.mazibahrami.example.util.JsonUtil
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeApiService
@Inject
constructor(
    private val jsonUtil: JsonUtil
) : ApiService {

    var blogPostsJsonFileName = Constants.BLOG_POSTS_DATA_FILENAME
    var categoriesFileName = Constants.CATEGORIES_DATA_FILENAME
    var networkDelay = 0L

    override suspend fun getBlogPosts(category: String): List<BlogPost> {
        val rawJson = jsonUtil.readJSONFromAsset(blogPostsJsonFileName)
        val blogs = Gson().fromJson<List<BlogPost>>(
            rawJson,
            object : TypeToken<List<BlogPost>>() {}.type
        )
        val filteredBlogPost = blogs.filter { blogPost -> blogPost.category == category }
        delay(networkDelay)
        return filteredBlogPost
    }

    override suspend fun getAllBlogPosts(): List<BlogPost> {
        val rawJson = jsonUtil.readJSONFromAsset(blogPostsJsonFileName)
        val blogs = Gson().fromJson<List<BlogPost>>(
            rawJson,
            object : TypeToken<List<BlogPost>>() {}.type
        )
        delay(networkDelay)
        return blogs
    }

    override suspend fun getCategories(): List<Category> {
        val rawJson = jsonUtil.readJSONFromAsset(categoriesFileName)
        val categories = Gson().fromJson<List<Category>>(
            rawJson,
            object : TypeToken<List<BlogPost>>() {}.type
        )
        delay(networkDelay)
        return categories
    }
}