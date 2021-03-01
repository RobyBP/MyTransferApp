package com.robybp.mytransferapp.screen.pickdriver

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.datamodels.Driver
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PickDriverFragment : Fragment(), PickDriverAdapter.OnClickListener {

    private lateinit var cancelButton: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var addNewButton: Button
    private var drivers = listOf<Driver>()
    private val adapter = PickDriverAdapter(this)
    private val model: PickDriverViewModel by sharedViewModel()
    private val compositeDisposable = CompositeDisposable()

    companion object {
        private const val PICK_CONTACT_CODE = 675
        const val TAG = "PickDriverFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_drivers_menu, container, false)
        initialiseViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        cancelButton.setOnClickListener {
            model.goBack()
        }

        addNewButton.setOnClickListener {
            openContacts()
        }

        compositeDisposable.add(model.allDrivers.subscribe {
            drivers = it
            adapter.setDrivers(it)
        })
        super.onViewCreated(view, savedInstanceState)
    }

    private fun openContacts() {
        if (requireActivity().checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                requireContext(),
                "The app doesn't have the permission to read contacts",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, PICK_CONTACT_CODE)
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_CONTACT_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                val uri: Uri = data!!.data!!
                val contentResolver: ContentResolver = requireActivity().contentResolver
                val contentCursor = contentResolver.query(uri, null, null, null, null)

                if (contentCursor!!.moveToFirst()) {
                    val id = contentCursor.getString(
                        contentCursor.getColumnIndexOrThrow(
                            ContactsContract.Contacts._ID
                        )
                    )
                    val hasPhone = contentCursor.getString(
                        contentCursor.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER
                        )
                    )

                    if (hasPhone == ("1")) {
                        val phones: Cursor? = requireActivity().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null,
                            null
                        )
                        phones!!.moveToFirst()
                        val contactNumber: String =
                            phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val name: String =
                            phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

                        if (model.noDuplicates(drivers, name)) model.saveDriver(
                            Driver(
                                0,
                                name,
                                contactNumber
                            )
                        ) else showAlertDialog(contactNumber)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showAlertDialog(phoneNumber: String): Boolean {
        var isValid = false
        val dialog = AlertDialog.Builder(requireContext())
        val et = EditText(requireContext())
        dialog.setTitle(resources.getString(R.string.driver_already_exists))
        dialog.setView(et)
        dialog.setPositiveButton(resources.getString(R.string.save)) { _, _ ->
            if (!model.noDuplicates(drivers, et.text.toString())) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.driver_also_exists),
                    Toast.LENGTH_LONG
                ).show()
                isValid = false
                return@setPositiveButton
            } else if (et.text.toString().isEmpty() || et.text.toString().isBlank())
                Toast.makeText(requireContext(), resources.getString(R.string.name_cannot_be_empty), Toast.LENGTH_LONG).show()
            else {
                model.saveDriver(Driver(0, et.text.toString(), phoneNumber))
            }
        }
        dialog.setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
            isValid = true
            return@setNegativeButton
        }
        dialog.show()
        return isValid
    }

    private fun initialiseViews(view: View) {
        cancelButton = view.findViewById(R.id.driversmenuscreen_cancel_button)
        recyclerView = view.findViewById(R.id.driversmenuscreen_recyclerview)
        addNewButton = view.findViewById(R.id.driversmenuscreen_addDriver_button)
        recyclerView.adapter = adapter
    }

    override fun onItemClicked(name: String) {
        model.setName(name)
        model.goBack()
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }
}