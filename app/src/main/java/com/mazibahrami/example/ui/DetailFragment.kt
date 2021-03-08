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
import com.mazibahrami.example.models.BlogPost
import com.mazibahrami.example.R
import com.mazibahrami.example.databinding.FragmentDetailBinding
import com.mazibahrami.example.ui.viewmodel.MainViewModel
import com.mazibahrami.example.util.GlideManager
import kotlinx.coroutines.*
import java.lang.Exception

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class DetailFragment
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: GlideManager
) : Fragment() {

    private val CLASS_NAME = "DetailFragment"

    lateinit var uiCommunicationListener: UICommunicationListener

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    val viewModel: MainViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()

        binding.blogImage.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_finalFragment)
        }

        initUI()
    }

    private fun initUI() {
        uiCommunicationListener.showStatusBar()
        uiCommunicationListener.expandAppBar()
        uiCommunicationListener.hideCategoriesMenu()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.detailFragmentView.selectedBlogPost?.let { selectedBlogPost ->
//                    printLogD(CLASS_NAME, "$selectedBlogPost")
                    setBlogPostToView(selectedBlogPost)
                }
            }
        })
    }

    private fun setBlogPostToView(blogPost: BlogPost) {
        requestManager
            .setImage(blogPost.image, binding.blogImage)
        binding.blogTitle.text = blogPost.title
        binding.blogCategory.text = blogPost.category
        binding.blogBody.text = blogPost.body
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


















