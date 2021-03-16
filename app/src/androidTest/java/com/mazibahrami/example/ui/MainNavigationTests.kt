package com.mazibahrami.example.ui

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mazibahrami.example.R
import com.mazibahrami.example.TestBaseApplication
import com.mazibahrami.example.di.TestAppComponent
import com.mazibahrami.example.models.BlogPost
import com.mazibahrami.example.ui.BlogPostListAdapter.BlogPostViewHolder
import com.mazibahrami.example.util.Constants
import com.mazibahrami.example.util.EspressoIdlingResourceRule
import com.mazibahrami.example.util.JsonUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class MainNavigationTests : BaseMainActivityTests() {

    @get:Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject
    lateinit var jsonUtil: JsonUtil

    @Test
    fun basicNavigationTest() {
        val app = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TestBaseApplication

        val apiService = configureFakeApiService(
            blogsDataSource = Constants.BLOG_POSTS_DATA_FILENAME,
            categoriesDataSource = Constants.CATEGORIES_DATA_FILENAME,
            networkDelay = 0L,
            application = app
        )

        configureFakeRepository(apiService, app)

        injectTest(app)

        val rawJson = jsonUtil.readJSONFromAsset(Constants.BLOG_POSTS_DATA_FILENAME)
        val blogs = Gson().fromJson<List<BlogPost>>(
            rawJson,
            object : TypeToken<List<BlogPost>>() {}.type
        )
        val SELECTED_LIST_INDEX = 8
        val selectedBlogPost = blogs[SELECTED_LIST_INDEX]

        val scenario = launchActivity<MainActivity>()

        val recyclerView = onView(withId(R.id.recycler_view))

        recyclerView.check(matches(isDisplayed()))

        recyclerView.perform(
            RecyclerViewActions.scrollToPosition<BlogPostViewHolder>(SELECTED_LIST_INDEX)
        )

        // nav to DetailFragment
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<BlogPostViewHolder>(
                SELECTED_LIST_INDEX,
                click()
            )
        )
        onView(withId(R.id.blog_title)).check(matches(withText(selectedBlogPost.title)))
        onView(withId(R.id.blog_category)).check(matches(withText(selectedBlogPost.category)))
        onView(withId(R.id.blog_body)).check(matches(withText(selectedBlogPost.body)))

        // nav to FinalFragment
        onView(withId(R.id.blog_image)).perform(click())
        onView(withId(R.id.scaling_image_view)).check(matches(isDisplayed()))

        // back to DetailFragment
        pressBack()
        onView(withId(R.id.blog_title)).check(matches(withText(selectedBlogPost.title)))

        // back to ListFragment
        pressBack()

        recyclerView.check(matches(isDisplayed()))
    }

    override fun injectTest(application: TestBaseApplication) {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }
}