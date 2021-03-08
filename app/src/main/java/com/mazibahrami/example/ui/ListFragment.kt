package com.mazibahrami.example.ui


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mazibahrami.example.models.BlogPost
import com.mazibahrami.example.R
import com.mazibahrami.example.databinding.FragmentListBinding
import com.mazibahrami.example.ui.viewmodel.*
import com.mazibahrami.example.ui.viewmodel.state.MainStateEvent.*
import com.mazibahrami.example.ui.viewmodel.state.MainViewState
import com.mazibahrami.example.util.GlideManager
import com.mazibahrami.example.util.TopSpacingItemDecoration
import kotlinx.coroutines.*
import java.lang.Exception

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ListFragment
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: GlideManager
) : Fragment(),
    BlogPostListAdapter.Interaction,
    SwipeRefreshLayout.OnRefreshListener {

    private val CLASS_NAME = "ListFragment"

    lateinit var uiCommunicationListener: UICommunicationListener

    lateinit var listAdapter: BlogPostListAdapter

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    val viewModel: MainViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeRefresh.setOnRefreshListener(this)
        initRecyclerView()
        subscribeObservers()
        initData()
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun saveLayoutManagerState() {
        binding.recyclerView.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    fun restoreLayoutManager() {
        viewModel.getLayoutManagerState()?.let { lmState ->
            binding.recyclerView.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    private fun initData() {
        val viewState = viewModel.getCurrentViewStateOrNew()
        if (viewState.listFragmentView.blogs == null
            || viewState.listFragmentView.categories == null
        ) {
            viewModel.setStateEvent(GetAllBlogs())
            viewModel.setStateEvent(GetCategories())
        }
    }

    /*
     I'm creating an observer in this fragment b/c I want more control
     over it. When a blog is selected I immediately stop observing.
     Mainly for hiding the menu in DetailFragment.
     "uiCommunicationListener.hideCategoriesMenu()"
    */
    val observer: Observer<MainViewState> = Observer { viewState ->
        if (viewState != null) {

            viewState.listFragmentView.let { view ->
                view.blogs?.let { blogs ->
                    listAdapter.apply {
                        submitList(blogs)
                    }
                    displayTheresNothingHereTV((blogs.size > 0))
                }
                view.categories?.let { categories ->
                    uiCommunicationListener.showCategoriesMenu(
                        categories = ArrayList(categories)
                    )
                }
            }
        }
    }

    private fun displayTheresNothingHereTV(isDataAvailable: Boolean) {
        if (isDataAvailable) {
           binding.noDataTextview.visibility = View.GONE
        } else {
            binding.noDataTextview.visibility = View.VISIBLE
        }
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, observer)
    }

    override fun onRefresh() {
        initData()
        binding.swipeRefresh.isRefreshing = false
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ListFragment.context)
            addItemDecoration(TopSpacingItemDecoration(30))
            listAdapter = BlogPostListAdapter(requestManager, this@ListFragment)
            adapter = listAdapter
        }
    }

    override fun restoreListPosition() {
        restoreLayoutManager()
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        removeViewStateObserver()
        viewModel.setSelectedBlogPost(blogPost = item)
        findNavController().navigate(R.id.action_listFragment_to_detailFragment)
    }

    private fun removeViewStateObserver() {
        viewModel.viewState.removeObserver(observer)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setUICommunicationListener(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun setUICommunicationListener(mockUICommuncationListener: UICommunicationListener?) {

        // TEST: Set interface from mock
        if (mockUICommuncationListener != null) {
            this.uiCommunicationListener = mockUICommuncationListener
        } else { // PRODUCTION: if no mock, get from context
            try {
                uiCommunicationListener = (context as UICommunicationListener)
            } catch (e: Exception) {
                Log.e(CLASS_NAME, "$context must implement UICommunicationListener")
            }
        }
    }

}