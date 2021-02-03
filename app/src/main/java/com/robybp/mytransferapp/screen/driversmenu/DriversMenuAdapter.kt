package com.robybp.mytransferapp.screen.driversmenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.datamodels.Driver

class DriversMenuAdapter : RecyclerView.Adapter<DriversMenuAdapter.DriversMenuViewHolder>() {

    inner class DriversMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val driverName: EditText = itemView.findViewById(R.id.driversmenuscreen_driverName_editText)
        val driverNumber: EditText =
            itemView.findViewById(R.id.driversmenuscreen_driverPhoneNumber_editText)

        init {
            driverName.inputType = EditorInfo.TYPE_NULL
            driverNumber.inputType = EditorInfo.TYPE_NULL
        }
    }

    private var driverList = listOf<Driver>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriversMenuViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.driversmenu_item, parent, false)
        return DriversMenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: DriversMenuViewHolder, position: Int) {
        holder.driverName.setText(driverList[position].name)
        holder.driverNumber.setText(driverList[position].phoneNumber)
    }

    override fun getItemCount(): Int {
        return driverList.size
    }

    fun setDrivers(drivers: List<Driver>) {
        driverList = drivers
        notifyDataSetChanged()
    }
}
