package com.example.ktorcontact.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ktorcontact.utils.MyDiffUtil
import com.example.ktorcontact.models.ContactResponse
import com.example.ktorcontact.databinding.ItemContactsBinding

class RVContactsAdapter(
    private val rvClickContactsAdapter: RVClickContactsAdapter
) :
    RecyclerView.Adapter<RVContactsAdapter.VH>() {

    private var oldContactList = arrayListOf<ContactResponse>()

    inner class VH(private val itemRV: ItemContactsBinding) : RecyclerView.ViewHolder(itemRV.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(contact: ContactResponse) {
            itemRV.tvName.text = "${contact.name} ${contact.surname}"
            itemRV.tvNumber.text = contact.number

            itemRV.imageMenu.setOnClickListener {
                rvClickContactsAdapter.menu(contact, it)
            }

            itemRV.root.setOnClickListener {
                rvClickContactsAdapter.click(contact)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemContactsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(oldContactList[position])
    }

    override fun getItemCount(): Int = oldContactList.size

    fun setData(newContactList: ArrayList<ContactResponse>) {
        val diffUtil = MyDiffUtil(oldContactList, newContactList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldContactList = newContactList
        diffResult.dispatchUpdatesTo(this)
    }

    interface RVClickContactsAdapter {
        fun click(contactResponse: ContactResponse)
        fun menu(contactResponse: ContactResponse, view: View)
    }

}
