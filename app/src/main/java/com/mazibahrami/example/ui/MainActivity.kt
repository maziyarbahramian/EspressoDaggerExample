package com.mazibahrami.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.mazibahrami.example.models.Category
import com.google.android.material.appbar.AppBarLayout
import com.mazibahrami.example.BaseApplication
import com.mazibahrami.example.R
import com.mazibahrami.example.databinding.ActivityMainBinding
import com.mazibahrami.example.ui.viewmodel.*
import com.mazibahrami.example.ui.viewmodel.state.MAIN_VIEW_STATE_BUNDLE_KEY
import com.mazibahrami.example.ui.viewmodel.state.MainStateEvent.*
import com.mazibahrami.example.ui.viewmodel.state.MainViewState
import com.mazibahrami.example.util.ERROR_STACK_BUNDLE_KEY
import com.mazibahrami.example.util.ErrorStack
import com.mazibahrami.example.util.ErrorState
import com.mazibahrami.example.util.printLogD
import kotlinx.coroutines.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainActivity : AppCompatActivity(),
    UICommunicationListener {

    private val CLASS_NAME = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    // keep reference of dialogs for dismissing if activity destroyed
    // also prevent recreation of same dialog when activity recreated
    private val dialogs: HashMap<String, MaterialDialog> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApplication).appComponent
            .inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        subscribeObservers()

        restoreInstanceState(savedInstanceState)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.let { inState ->
            (inState[MAIN_VIEW_STATE_BUNDLE_KEY] as MainViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
            (inState[ERROR_STACK_BUNDLE_KEY] as ArrayList<ErrorState>?)?.let { stack ->
                val errorStack = ErrorStack()
                errorStack.addAll(stack)
                viewModel.setErrorStack(errorStack)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.clearActiveJobCounter()
        outState.putParcelable(
            MAIN_VIEW_STATE_BUNDLE_KEY,
            viewModel.getCurrentViewStateOrNew()
        )
        outState.putParcelableArrayList(
            ERROR_STACK_BUNDLE_KEY,
            viewModel.errorStack
        )
        super.onSaveInstanceState(outState)
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(this, Observer { viewState ->
            if (viewState != null) {
//                uiCommunicationListener.displayMainProgressBar(viewModel.areAnyJobsActive())
                displayMainProgressBar(viewModel.areAnyJobsActive())
            }
        })

        viewModel.errorState.observe(this, Observer { errorState ->
            errorState?.let {
                displayErrorMessage(errorState)
            }
        })
    }

    private fun displayErrorMessage(errorState: ErrorState) {
        if (!dialogs.containsKey(errorState.message)) {
            dialogs.put(
                errorState.message,
                displayErrorDialog(errorState.message, object : ErrorDialogCallback {
                    override fun clearError() {
                        viewModel.clearError(0)
                    }
                })
            )
        }
    }

    private fun setupActionBar() {
        binding.toolBar.setupWithNavController(
            findNavController(R.id.nav_host_fragment)
        )
    }

    private fun onMenuItemSelected(categories: List<Category>, menuItem: MenuItem): Boolean {
        for (category in categories) {
            if (category.pk == menuItem.itemId) {
                viewModel.clearLayoutManagerState()
                if (category.category_name.equals(MENU_ITEM_NAME_GET_ALL_BLOGS)) {
                    viewModel.setStateEvent(GetAllBlogs())
                } else {
                    viewModel.setStateEvent(SearchBlogsByCategory(category.category_name))
                }
                return true
            }
        }
        return false
    }

    override fun showCategoriesMenu(categories: ArrayList<Category>) {
        printLogD(CLASS_NAME, "showCategoriesMenu: ${categories}")
        val menu = binding.toolBar.menu
        menu.clear()
        categories.add(Category(MENU_ITEM_ID_GET_ALL_BLOGS, MENU_ITEM_NAME_GET_ALL_BLOGS))
        for ((index, category) in categories.withIndex()) {
            menu.add(0, category.pk, index, category.category_name)
        }
        binding.toolBar.invalidate()
        binding.toolBar.setOnMenuItemClickListener { menuItem ->
            onMenuItemSelected(categories, menuItem)
        }
    }

    override fun hideCategoriesMenu() {
        printLogD(CLASS_NAME, "hideCategoriesMenu")
        binding.toolBar.menu.clear()
        binding.toolBar.invalidate()
    }

    override fun displayMainProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.mainProgressBar.visibility = View.VISIBLE
        } else {
            binding.mainProgressBar.visibility = View.GONE
        }
    }

    override fun hideToolbar() {
        binding.toolBar.visibility = View.GONE
    }

    override fun showToolbar() {
        binding.toolBar.visibility = View.VISIBLE
    }

    override fun hideStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        hideToolbar()
    }

    override fun showStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        showToolbar()
    }

    override fun expandAppBar() {
        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }

    override fun onDestroy() {
        cleanUpOnDestroy()
        super.onDestroy()
    }

    private fun cleanUpOnDestroy() {
        for (dialog in dialogs) {
            dialog.value.dismiss()
        }
    }

    companion object {

        const val MENU_ITEM_ID_GET_ALL_BLOGS = 99999999
        const val MENU_ITEM_NAME_GET_ALL_BLOGS = "All"
    }

}


















