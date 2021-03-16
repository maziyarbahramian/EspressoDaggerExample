package com.mazibahrami.example.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry
import com.mazibahrami.example.R
import com.mazibahrami.example.TestBaseApplication
import com.mazibahrami.example.di.TestAppComponent
import com.mazibahrami.example.fragments.FakeMainFragmentFactory
import com.mazibahrami.example.ui.BlogPostListAdapter.BlogPostViewHolder
import com.mazibahrami.example.util.Constants
import com.mazibahrami.example.util.EspressoIdlingResourceRule
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class ListFragmentNavigationTest : BaseMainActivityTests() {

    @get:Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject
    lateinit var fragmentFactory: FakeMainFragmentFactory

    val uiCommunicationListener = mockk<UICommunicationListener>()

    @Before
    fun init() {
        every { uiCommunicationListener.showStatusBar() } just runs
        every { uiCommunicationListener.expandAppBar() } just runs
        every { uiCommunicationListener.hideCategoriesMenu() } just runs
        every { uiCommunicationListener.showCategoriesMenu(any()) } just runs
    }

    @Test
    fun testNavigationToDetailFragment() {
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

        fragmentFactory.uiCommunicationListener = uiCommunicationListener

        val navController = TestNavHostController(app)
        runOnUiThread {
            navController.setGraph(R.navigation.main_nav_graph)
            navController.setCurrentDestination(R.id.listFragment)
        }

        val scenario = launchFragmentInContainer<ListFragment>(factory = fragmentFactory)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        val recyclerView = onView(withId(R.id.recycler_view))
        recyclerView.check(matches(isDisplayed()))
        recyclerView.perform(RecyclerViewActions.scrollToPosition<BlogPostViewHolder>(5))
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<BlogPostViewHolder>(
                5,
                click()
            )
        )
        assertEquals(navController.currentDestination?.id, R.id.detailFragment)
    }

    override fun injectTest(application: TestBaseApplication) {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }
}