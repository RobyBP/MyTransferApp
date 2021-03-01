package com.robybp.mytransferapp.screen.newapartment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.datamodels.Apartment
import com.robybp.mytransferapp.screen.pickdriver.PickDriverViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class
NewApartmentFragment : Fragment() {

    private lateinit var apartmentNameEditText: EditText
    private lateinit var apartmentCityEditText: EditText
    private lateinit var apartmentAddressEditText: EditText
    private lateinit var ownerEditText: EditText
    private lateinit var ownerNumberEditText: EditText
    private lateinit var noteEditText: EditText
    private lateinit var saveButton: View
    private lateinit var cancelButton: View
    private lateinit var deleteButton: View
    private lateinit var sendInfoButton: View
    private val model: NewApartmentViewModel by viewModel()
    private val sharedPickDriverViewModel: PickDriverViewModel by sharedViewModel()
    private var inputFields = listOf<EditText>()
    private val compositeDisposable = CompositeDisposable()

    companion object {
        const val TAG = "NewApartmentFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_new_apartment, container, false)
        initialiseViews(view)
        sharedPickDriverViewModel.setName(null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        cancelButton.setOnClickListener { model.goBack() }

        saveButton.setOnClickListener { validateFieldsAndSaveApartment() }

        sharedPickDriverViewModel.getName().observe(viewLifecycleOwner,
            { if (it != null) showSendInfoDialog() })

        sendInfoButton.setOnClickListener {
            model.goToPickDriverFragment()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun validateFieldsAndSaveApartment() {
        if (model.crucialFieldsEmpty(inputFields)) Toast.makeText(requireContext(), "Only note field can be empty", Toast.LENGTH_LONG).show()
        else{
            model.saveApartment(
                Apartment(
                    id = 0,
                    name = apartmentNameEditText.text.toString(),
                    city = apartmentCityEditText.text.toString(),
                    address = apartmentAddressEditText.text.toString(),
                    owner = ownerEditText.text.toString(),
                    ownerPhoneNumber = ownerNumberEditText.text.toString(),
                    note = noteEditText.text.toString()
                )
            )
            model.goBack()
        }
    }

    private fun showSendInfoDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Send apartment info to ${sharedPickDriverViewModel.getName().value}?")
        dialog.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
            sendInfo()
        }
        dialog.setNegativeButton(resources.getString(R.string.no)) { _, _ ->
            sharedPickDriverViewModel.setName(null)
            return@setNegativeButton
        }
        dialog.show()
    }

    private fun sendInfo() {

        if (model.crucialFieldsEmpty(inputFields)) {
            Toast.makeText(requireContext(), "Only note field can be empty", Toast.LENGTH_LONG)
                .show()
            return
        }

        if (sharedPickDriverViewModel.getName().value == null) return

        val apartment = Apartment(
            id = 0,
            name = apartmentNameEditText.text.toString(),
            city = apartmentCityEditText.text.toString(),
            address = apartmentAddressEditText.text.toString(),
            owner = ownerEditText.text.toString(),
            ownerPhoneNumber = ownerNumberEditText.text.toString(),
            note = noteEditText.text.toString()
        )

        compositeDisposable.add(
            model.getDriverByName(sharedPickDriverViewModel.getName().value!!)
                .doOnSuccess { Log.d("driver", it.name + it.phoneNumber) }
                .subscribe { formatMessage(it.phoneNumber, apartment) }
        )
    }

    private fun formatMessage(phoneNumber: String, apartment: Apartment) {

        var note = ""

        if (!apartment.note.isNullOrBlank()) note = "\n" + apartment.note

        val messageBody = resources.getString(
            R.string.apartmentInfo_messageBody,
            resources.getString(R.string.apartment_name),
            apartment.name,
            resources.getString(R.string.apartment_city_hint),
            apartment.city,
            resources.getString(R.string.address),
            apartment.address,
            resources.getString(R.string.owner),
            apartment.owner,
            resources.getString(R.string.phone),
            apartment.ownerPhoneNumber,
            note
        )

        model.sendMessage(messageBody, phoneNumber)
        model.saveApartment(apartment)
        model.goBack()
    }

    private fun initialiseViews(view: View) {
        apartmentNameEditText = view.findViewById(R.id.newapartment_apartmentName_editText)
        apartmentCityEditText = view.findViewById(R.id.newapartment_city_editText)
        apartmentAddressEditText = view.findViewById(R.id.newapartment_address_editText)
        ownerEditText = view.findViewById(R.id.newapartment_owner_editText)
        ownerNumberEditText = view.findViewById(R.id.newapartment_phone_editText)
        noteEditText = view.findViewById(R.id.newapartment_note_editText)
        saveButton = view.findViewById(R.id.newapartment_save_button)
        cancelButton = view.findViewById(R.id.newapartment_cancel_button)
        deleteButton = view.findViewById(R.id.newapartment_delete_button)
        deleteButton.visibility = View.GONE
        sendInfoButton = view.findViewById(R.id.newapartment_sendinfo_button)
        inputFields = listOf(apartmentNameEditText, apartmentCityEditText, apartmentAddressEditText, ownerEditText, ownerNumberEditText)
    }
}
