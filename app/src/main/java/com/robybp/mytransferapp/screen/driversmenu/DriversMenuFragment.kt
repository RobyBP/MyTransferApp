package com.robybp.mytransferapp.screen.driversmenu

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.datamodels.Driver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class DriversMenuFragment : Fragment() {

    private lateinit var addNewButton: Button
    private lateinit var exitButton: View
    private val model: DriversMenuViewModel by inject()
    private lateinit var recyclerView: RecyclerView

    private val compositeDisposable = CompositeDisposable()
    private val adapter = DriversMenuAdapter()

    companion object {
        val TAG = "DriversMenuFragment"
        private const val PICK_CONTACT_CODE = 123
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

        compositeDisposable.add(
            model.allDrivers
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.setDrivers(it)
                }
        )

        addNewButton.setOnClickListener {
            openContacts()
        }

        exitButton.setOnClickListener {
            model.goBack()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun openContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requireActivity().checkSelfPermission(
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

                        model.saveDriver(
                            Driver(
                                driverId = 0,
                                name = name,
                                phoneNumber = contactNumber
                            )
                        )
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initialiseViews(view: View) {
        addNewButton = view.findViewById(R.id.driversmenuscreen_addDriver_button)
        exitButton = view.findViewById(R.id.driversmenuscreen_cancel_button)
        recyclerView = view.findViewById(R.id.driversmenuscreen_recyclerview)
        recyclerView.adapter = adapter
    }
}
