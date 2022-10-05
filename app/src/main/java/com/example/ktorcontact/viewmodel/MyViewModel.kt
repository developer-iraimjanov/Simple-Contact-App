package com.example.ktorcontact.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ktorcontact.models.ContactRequest
import com.example.ktorcontact.models.ContactResponse
import com.example.ktorcontact.models.Response
import com.example.ktorcontact.network.KtorService
import kotlinx.coroutines.launch

class MyViewModel (
    private val ktorService: KtorService
) : ViewModel() {

    fun getContacts(swipeRefreshLayout: SwipeRefreshLayout) {
        swipeRefreshLayout.isRefreshing = true
        viewModelScope.launch {
            getResponse.value = ktorService.getContacts()
        }
    }

    fun addContact(contactRequest: ContactRequest, swipeRefreshLayout: SwipeRefreshLayout) {
        swipeRefreshLayout.isRefreshing = true
        viewModelScope.launch {
            audResponse.value = ktorService.addContacts(contactRequest)
        }
    }

    fun updateContact(contactResponse: ContactResponse, swipeRefreshLayout: SwipeRefreshLayout) {
        swipeRefreshLayout.isRefreshing = true
        viewModelScope.launch {
            audResponse.value = ktorService.updateContacts(contactResponse)
        }
    }

    fun deleteContact(contactResponse: ContactResponse, swipeRefreshLayout: SwipeRefreshLayout) {
        swipeRefreshLayout.isRefreshing = true
        viewModelScope.launch {
            audResponse.value = ktorService.deleteContacts(contactResponse.id)
        }
    }

    var getResponse = MutableLiveData<List<ContactResponse>>()
    var audResponse = MutableLiveData<Response>()

}