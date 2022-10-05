package com.example.ktorcontact.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import com.example.ktorcontact.R
import com.example.ktorcontact.adapters.RVContactsAdapter
import com.example.ktorcontact.databinding.ActivityMainBinding
import com.example.ktorcontact.databinding.AddDialogBinding
import com.example.ktorcontact.models.ContactRequest
import com.example.ktorcontact.models.ContactResponse
import com.example.ktorcontact.network.KtorService
import com.example.ktorcontact.viewmodel.MyViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialog: AlertDialog
    private lateinit var rvContactsAdapter: RVContactsAdapter
    private lateinit var vm: MyViewModel
    private var booleanAntiBagDialog = true
    private var booleanAntiBagPopup = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.swipeRefreshLayout.isEnabled = false
        vm = MyViewModel(KtorService.create())

        binding.imageAdd.setOnClickListener {
            if (booleanAntiBagDialog) {
                buildAddDialog()
                booleanAntiBagDialog = false
            }
        }

        buildRV()

        vm.getContacts(binding.swipeRefreshLayout)

        responses()

    }

    private fun responses() {

        vm.audResponse.observe(this) {
            binding.swipeRefreshLayout.isRefreshing = false
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            dialog.cancel()
            if (!it.error) {
                vm.getContacts(binding.swipeRefreshLayout)
            }
        }

        vm.getResponse.observe(this) {
            binding.swipeRefreshLayout.isRefreshing = false
            rvContactsAdapter.setData(it as ArrayList<ContactResponse>)
        }

    }

    private fun buildAddDialog() {
        val alertDialog = AlertDialog.Builder(this)
        val dialogBinding = AddDialogBinding.inflate(layoutInflater)

        dialogBinding.lyDone.setOnClickListener {
            val firstname = dialogBinding.edtFirstname.text.toString().trim()
            val lastname = dialogBinding.edtLastname.text.toString().trim()
            val number = dialogBinding.edtNumber.text.toString().trim()
            if (firstname.isNotEmpty() && lastname.isNotEmpty() && number.isNotEmpty()) {
                dialogBinding.imageDone.visibility = View.INVISIBLE
                dialogBinding.progressCircular.visibility = View.VISIBLE
                vm.addContact(
                    ContactRequest(firstname, lastname, number),
                    binding.swipeRefreshLayout
                )
            }
        }

        alertDialog.setView(dialogBinding.root)

        alertDialog.setOnCancelListener {
            booleanAntiBagDialog = true
        }

        dialog = alertDialog.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    private fun buildUpdateDialog(contactResponse: ContactResponse) {
        val alertDialog = AlertDialog.Builder(this)
        val dialogBinding = AddDialogBinding.inflate(layoutInflater)

        dialogBinding.edtFirstname.setText(contactResponse.name)
        dialogBinding.edtLastname.setText(contactResponse.surname)
        dialogBinding.edtNumber.setText(contactResponse.number)

        dialogBinding.lyDone.setOnClickListener {
            val firstname = dialogBinding.edtFirstname.text.toString().trim()
            val lastname = dialogBinding.edtLastname.text.toString().trim()
            val number = dialogBinding.edtNumber.text.toString().trim()
            if (firstname.isNotEmpty() && lastname.isNotEmpty() && number.isNotEmpty()) {
                dialogBinding.imageDone.visibility = View.INVISIBLE
                dialogBinding.progressCircular.visibility = View.VISIBLE
                vm.updateContact(
                    ContactResponse(contactResponse.id, firstname, lastname, number),
                    binding.swipeRefreshLayout
                )
            }
        }

        alertDialog.setView(dialogBinding.root)

        alertDialog.setOnCancelListener {
            booleanAntiBagDialog = true
        }

        dialog = alertDialog.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    private fun buildRV() {
        rvContactsAdapter = RVContactsAdapter(
            object : RVContactsAdapter.RVClickContactsAdapter {
                override fun click(contactResponse: ContactResponse) {
                    makeCall(contactResponse.number)
                }

                override fun menu(contactResponse: ContactResponse, view: View) {
                    if (booleanAntiBagPopup) {
                        buildPopupMenu(contactResponse, view)
                        booleanAntiBagPopup = false
                    }
                }
            })
        binding.rv.adapter = rvContactsAdapter
    }

    override fun attachBaseContext(newBase: Context?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.attachBaseContext(newBase)
    }

    @SuppressLint("RestrictedApi")
    private fun buildPopupMenu(
        contactResponse: ContactResponse,
        view: View
    ) {
        val menuBuilder = MenuBuilder(this)
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.popup_menu, menuBuilder)
        val menuPopupHelper = MenuPopupHelper(this, menuBuilder, view)
        menuPopupHelper.setForceShowIcon(true)
        menuBuilder.setCallback(object : MenuBuilder.Callback {
            override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.menu_edit -> {
                        if (booleanAntiBagDialog) {
                            buildUpdateDialog(contactResponse)
                            booleanAntiBagDialog = false
                        }
                    }

                    R.id.menu_delete -> {
                        if (booleanAntiBagDialog) {
                            buildDeleteDialog(contactResponse)
                            booleanAntiBagDialog = false
                        }
                    }
                }
                return true
            }

            override fun onMenuModeChange(menu: MenuBuilder) {}
        })

        menuPopupHelper.setOnDismissListener {
            booleanAntiBagPopup = true
        }

        menuPopupHelper.show()
    }

    private fun buildDeleteDialog(contactResponse: ContactResponse) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete")
            .setPositiveButton("Delete") { _, _ ->
                binding.swipeRefreshLayout.isRefreshing = true
                vm.deleteContact(contactResponse, binding.swipeRefreshLayout)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        builder.setOnDismissListener {
            booleanAntiBagDialog = true
        }

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun makeCall(number: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${number}"))
        startActivity(intent)
    }

}