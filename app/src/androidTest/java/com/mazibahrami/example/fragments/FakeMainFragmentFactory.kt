package com.mazibahrami.example.fragments


import androidx.fragment.app.FragmentFactory
import com.mazibahrami.example.ui.DetailFragment
import com.mazibahrami.example.ui.FinalFragment
import com.mazibahrami.example.ui.ListFragment
import com.mazibahrami.example.ui.UICommunicationListener
import com.mazibahrami.example.util.GlideManager
import com.mazibahrami.example.viewmodels.FakeMainViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Singleton
class FakeMainFragmentFactory
@Inject
constructor(
    private val viewModelFactory: FakeMainViewModelFactory,
    private val requestManager: GlideManager
) : FragmentFactory() {

    // used for setting a mock<UICommunicationListener>
    lateinit var uiCommunicationListener: UICommunicationListener

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            ListFragment::class.java.name -> {
                val fragment = ListFragment(viewModelFactory, requestManager)
                if (::uiCommunicationListener.isInitialized) {
                    fragment.setUICommunicationListener(uiCommunicationListener)
                }
                fragment
            }

            DetailFragment::class.java.name -> {
                val fragment = DetailFragment(viewModelFactory, requestManager)
                if (::uiCommunicationListener.isInitialized) {
                    fragment.setUICommunicationListener(uiCommunicationListener)
                }
                fragment
            }

            FinalFragment::class.java.name -> {
                val fragment = FinalFragment(viewModelFactory, requestManager)
                if (::uiCommunicationListener.isInitialized) {
                    fragment.setUICommunicationListener(uiCommunicationListener)
                }
                fragment
            }

            else -> {
                super.instantiate(classLoader, className)
            }
        }
}
