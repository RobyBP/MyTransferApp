package com.robybp.mytransferapp.screen.pickapartment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.datamodels.Apartment

class PickApartmentAdapter(private val clickListener: ClickListener): RecyclerView.Adapter<PickApartmentViewHolder>() {

    private var apartments = listOf<Apartment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickApartmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.apartment_item, parent, false)
        return PickApartmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PickApartmentViewHolder, position: Int) {
        holder.apartmentName.text = apartments[position].name
        holder.address.text = apartments[position].address
        holder.city.text = apartments[position].city
        holder.owner.text = apartments[position].owner
        holder.ownerContact.text = apartments[position].ownerPhoneNumber
        holder.settingsButton.visibility = View.GONE

        holder.itemView.setOnClickListener {
            clickListener.onItemClicked(apartments[position].name)
        }
    }

    override fun getItemCount(): Int {
        return apartments.size
    }

    fun setApartments(apartments: List<Apartment>) {
        this.apartments = apartments
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onItemClicked(apartmentName: String)
    }
}

class PickApartmentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val apartmentName: TextView = itemView.findViewById(R.id.apartmentsmenu_apartment_name)
    val address: TextView = itemView.findViewById(R.id.apartmentsmenu_apartment_address)
    val city: TextView = itemView.findViewById(R.id.apartmentsmenu_apartment_city)
    val owner: TextView = itemView.findViewById(R.id.apartmentsmenu_apartment_owner)
    val ownerContact: TextView = itemView.findViewById(R.id.apartmentsmenu_apartment_owner_contact)
    val settingsButton: View = itemView.findViewById(R.id.apartmentsmenu_settings_button)
}