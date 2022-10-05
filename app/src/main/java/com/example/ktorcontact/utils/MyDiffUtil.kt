package com.example.ktorcontact.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.ktorcontact.models.ContactResponse

class MyDiffUtil(
    private val oldContactList: ArrayList<ContactResponse>,
    private val newContactList: ArrayList<ContactResponse>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldContactList.size
    }

    override fun getNewListSize(): Int {
        return newContactList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldContactList[oldItemPosition].id == newContactList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldContactList[oldItemPosition].id != newContactList[newItemPosition].id -> {
                return false
            }
            oldContactList[oldItemPosition].name != newContactList[newItemPosition].name -> {
                return false
            }
            oldContactList[oldItemPosition].surname != newContactList[newItemPosition].surname -> {
                return false
            }
            oldContactList[oldItemPosition].number != newContactList[newItemPosition].number -> {
                return false
            }
            else -> true
        }
    }
}