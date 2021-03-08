package com.mazibahrami.example.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mazibahrami.example.databinding.FragmentFinalBinding
import com.mazibahrami.example.ui.viewmodel.MainViewModel
import com.mazibahrami.example.util.GlideManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import java.lang.Exception

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class FinalFragment
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: GlideManager
) : Fragment() {

    private val CLASS_NAME = "DetailFragment"

    lateinit var uiCommunicationListener: UICommunicationListener

    private var _binding: FragmentFinalBinding? = null
    private val binding get() = _binding!!

    val viewModel: MainViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        uiCommunicationListener.hideStatusBar()
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.detailFragmentView.selectedBlogPost?.let { blogPost ->
                    setImage(blogPost.image)
                }
            }
        })
    }

    private fun setImage(imageUrl: String) {
        requestManager.setImage(imageUrl, binding.scalingImageView)
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
