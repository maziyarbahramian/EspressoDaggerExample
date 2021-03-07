package com.mazibahrami.example.ui

import com.mazibahrami.example.models.Category

interface UICommunicationListener {

    fun showCategoriesMenu(categories: ArrayList<Category>)

    fun hideCategoriesMenu()

    fun displayMainProgressBar(isLoading: Boolean)

    fun hideToolbar()

    fun showToolbar()

    fun hideStatusBar()

    fun showStatusBar()

    fun expandAppBar()

}