package com.robybp.mytransferapp.screen.pickapartment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.robybp.mytransferapp.R
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import io.reactivex.disposables.CompositeDisposable

class PickApartmentFragment : Fragment(), PickApartmentAdapter.ClickListener {

    private lateinit var closeButton: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var newApartmentButton: FloatingActionButton
    private val model: PickApartmentViewModel by sharedViewModel()
    private val adapter: PickApartmentAdapter = PickApartmentAdapter(this)
    private val compositeDisposable = CompositeDisposable()

    companion object {
        const val TAG = "PickApartmentFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_apartments_menu, container, false)
        initialiseViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        compositeDisposable.add(
            model.apartments.subscribe { adapter.setApartments(it) }
        )

        newApartmentButton.setOnClickListener {
            model.goToCreateNewApartment()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialiseViews(view: View) {
        closeButton = view.findViewById(R.id.apartmentsmenu_close_icon)
        recyclerView = view.findViewById(R.id.apartmentsmenu_recyclerview)
        newApartmentButton = view.findViewById(R.id.apartmentsmenu_addApartment_button)
        recyclerView.adapter = adapter
    }

    override fun onItemClicked(apartmentName: String) {
        model.setApartmentName(apartmentName)
        model.goBack()
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }

}