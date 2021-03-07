package com.mazibahrami.example.fragments

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.mazibahrami.example.ui.DetailFragment
import com.mazibahrami.example.ui.FinalFragment
import com.mazibahrami.example.ui.ListFragment
import com.mazibahrami.example.util.GlideManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Singleton
class MainFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: GlideManager
): FragmentFactory(){

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when(className){

            ListFragment::class.java.name -> {
                val fragment = ListFragment(viewModelFactory, requestManager)
                fragment
            }

            DetailFragment::class.java.name -> {
                val fragment = DetailFragment(viewModelFactory, requestManager)
                fragment
            }

            FinalFragment::class.java.name -> {
                val fragment = FinalFragment(viewModelFactory, requestManager)
                fragment
            }

            else -> {
                super.instantiate(classLoader, className)
            }
        }
}












