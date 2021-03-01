package com.robybp.mytransferapp.screen.newapartment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.datamodels.Apartment
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
    private val model: NewApartmentViewModel by viewModel()
    private var inputFields = listOf<EditText>()

    companion object {
        const val TAG = "NewApartmentFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_new_apartment, container, false)
        initialiseViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        cancelButton.setOnClickListener { model.goBack() }

        saveButton.setOnClickListener { validateFieldsAndSaveApartment() }
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
        inputFields = listOf(apartmentNameEditText, apartmentCityEditText, apartmentAddressEditText, ownerEditText, ownerNumberEditText)
    }
}
