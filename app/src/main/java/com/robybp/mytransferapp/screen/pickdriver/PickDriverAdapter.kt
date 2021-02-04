package com.robybp.mytransferapp.screen.pickdriver

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.datamodels.Driver

class PickDriverAdapter(private val clickListener: OnClickListener) : RecyclerView.Adapter<PickDriverAdapter.PickDriverViewHolder>() {

    private var driverList = listOf<Driver>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickDriverViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.driversmenu_item, parent, false)
        return PickDriverViewHolder(view)
    }

    override fun onBindViewHolder(holder: PickDriverViewHolder, position: Int) {

        holder.driverName.setText(driverList[position].name)
        holder.phoneNumber.setText(driverList[position].phoneNumber)
        holder.itemView.setOnClickListener {
            clickListener.onItemClicked()
        }
    }

    override fun getItemCount(): Int {
        return driverList.size
    }

    fun setDrivers(drivers: List<Driver>){
        driverList = drivers
        notifyDataSetChanged()
    }

    interface OnClickListener{
        fun onItemClicked()
    }

    class PickDriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val driverName: EditText = itemView.findViewById(R.id.driversmenuscreen_driverName_editText)
        val phoneNumber: EditText =
            itemView.findViewById(R.id.driversmenuscreen_driverPhoneNumber_editText)

        init {
            driverName.inputType = EditorInfo.TYPE_NULL
            phoneNumber.inputType = EditorInfo.TYPE_NULL
        }
    }
}
