package com.example.ktorcontact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ktorcontact.network.KtorService

class MyViewModelFactory(private val ktorService: KtorService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            return MyViewModel(ktorService) as T
        }
        throw IllegalArgumentException("Error")
    }
}