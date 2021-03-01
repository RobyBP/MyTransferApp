package com.robybp.mytransferapp.screen.apartmentinfo

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.datamodels.Apartment
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class ApartmentInfoFragment : Fragment() {

    private lateinit var apartmentNameEditText: EditText
    private lateinit var apartmentCityEditText: EditText
    private lateinit var apartmentAddressEditText: EditText
    private lateinit var ownerEditText: EditText
    private lateinit var ownerNumberEditText: EditText
    private lateinit var noteEditText: EditText
    private lateinit var saveButton: View
    private lateinit var cancelButton: View
    private var inputFields = listOf<EditText>()
    private lateinit var deleteButton: View
    private val compositeDisposable = CompositeDisposable()
    private val model: ApartmentInfoViewModel by viewModel()
    private var apartmentId by Delegates.notNull<Int>()

    companion object {
        const val TAG = "ApartmentInfoFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_new_apartment, container, false)
        initialiseViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        compositeDisposable.add(
            model.queryApartmentByAddress(requireArguments().getString("Address")!!).subscribe {
                setInfo(it)
                apartmentId = it.id
            }
        )

        saveButton.setOnClickListener {
            validateFieldsAndUpdateApartment()
        }

        apartmentNameEditText.setOnClickListener {
            model.goToPickApartment()
        }

        deleteButton.setOnClickListener {
            showAlertDialog()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun validateFieldsAndUpdateApartment() {
        if (model.crucialFieldsEmpty(inputFields)) Toast.makeText(requireContext(), "Only note field can be empty", Toast.LENGTH_LONG).show()
        else {
            model.updateApartment(
                Apartment(
                    apartmentId,
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

    private fun showAlertDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(resources.getString(R.string.delete_apartment))
        dialog.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
            model.deleteApartment(apartmentId)
            model.goBack()
        }
        dialog.setNegativeButton(resources.getString(R.string.no)) { _, _ ->
            return@setNegativeButton
        }
        dialog.show()
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
        inputFields = listOf(apartmentNameEditText, apartmentCityEditText, apartmentAddressEditText, ownerEditText, ownerNumberEditText)
    }

    private fun setInfo(apartment: Apartment) {
        apartmentNameEditText.setText(apartment.name)
        apartmentCityEditText.setText(apartment.city)
        apartmentAddressEditText.setText(apartment.address)
        ownerEditText.setText(apartment.owner)
        ownerNumberEditText.setText(apartment.ownerPhoneNumber)
        noteEditText.setText(apartment.note)
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }
}
