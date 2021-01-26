package com.robybp.mytransferapp.fragments.homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robybp.mytransferapp.MeansOfTransport
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.models.datamodels.Guest

class HomeScreenAdapter(): RecyclerView.Adapter<HomeScreenAdapter.HomeScreenViewHolder>() {

    inner class HomeScreenViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val transportInfoHint = itemView.findViewById<TextView>(R.id.homescreen_transport_info_hint)
        val guestName: TextView = itemView.findViewById(R.id.homescreen_name)
        val transportInfo: TextView = itemView.findViewById(R.id.homescreen_transport_info)
        val country: TextView = itemView.findViewById(R.id.homescreen_city)
        val dateAndTimeOfArrival: TextView = itemView.findViewById(R.id.homescreen_date_and_time)
        val driver: TextView = itemView.findViewById(R.id.homescreen_driver)
    }

    private var guests = listOf<Guest>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeScreenAdapter.HomeScreenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.homescreen_item, parent, false)
        return HomeScreenViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeScreenAdapter.HomeScreenViewHolder, position: Int) {
        when(guests[position].meansOfTransport){
            MeansOfTransport.AIRPLANE.toString() -> holder.transportInfoHint.setText(R.string.messageInfo_flightNumber_hint)
            MeansOfTransport.BUS.toString() -> holder.transportInfoHint.setText(R.string.messageInfo_busCompany_hint)
            MeansOfTransport.SHIP.toString() -> holder.transportInfoHint.setText(R.string.messageInfo_shipNumber_hint)
            MeansOfTransport.TRAIN.toString() -> holder.transportInfoHint.setText(R.string.newGuest_trainNumber_hint)
        }

        holder.guestName.text = guests[position].name
        holder.transportInfo.text = guests[holder.adapterPosition].vehicleInfo
        holder.country.text = guests[holder.adapterPosition].countryOfArrival
        holder.dateAndTimeOfArrival.text = guests[holder.adapterPosition].dateOfArrival + " " + guests[holder.adapterPosition].timeOfArrival
        holder.driver.text = guests[holder.adapterPosition].driverName
    }

    override fun getItemCount(): Int {
        return guests.size
    }

    fun setGuests(guests: List<Guest>){
        this.guests = guests
        notifyDataSetChanged()
    }
}
