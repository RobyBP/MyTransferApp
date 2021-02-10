package com.robybp.mytransferapp.screen.pickdriver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.robybp.mytransferapp.R
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PickDriverFragment: Fragment(), PickDriverAdapter.OnClickListener {

    private lateinit var cancelButton: View
    private lateinit var recyclerView: RecyclerView
    private val adapter = PickDriverAdapter(this)
    private val model: PickDriverViewModel by sharedViewModel()
    private val compositeDisposable = CompositeDisposable()

    companion object{
        const val TAG = "PickDriverFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pick_driver, container, false)
        initialiseViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        cancelButton.setOnClickListener {
            model.goBack()
        }

        compositeDisposable.add(model.allDrivers.subscribe { adapter.setDrivers(it) })
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialiseViews(view: View){
        cancelButton = view.findViewById(R.id.pickdriverscreen_cancel_button)
        recyclerView = view.findViewById(R.id.pickdriverscreen_recyclerview)
        recyclerView.adapter = adapter
    }

    override fun onItemClicked(name: String) {
        model.driverName = name
        model.setName(name)
        model.goBack()
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }
}