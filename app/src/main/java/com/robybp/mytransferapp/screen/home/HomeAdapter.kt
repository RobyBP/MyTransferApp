package com.robybp.mytransferapp.screen.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransport

// TODO: ListAdapter instead of RecyclerView.Adapter
class HomeAdapter(private val clickListener: OnItemClicked) :
    RecyclerView.Adapter<HomeScreenViewHolder>() {

    private var guests = listOf<Guest>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeScreenViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.homescreen_item, parent, false)
        return HomeScreenViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeScreenViewHolder, position: Int) {
        when (guests[position].meansOfTransport) {
            MeansOfTransport.AIRPLANE.toString() -> holder.transportInfoHint.setText(R.string.messageInfo_flightNumber_hint)
            MeansOfTransport.BUS.toString() -> holder.transportInfoHint.setText(R.string.messageInfo_busCompany_hint)
            MeansOfTransport.SHIP.toString() -> holder.transportInfoHint.setText(R.string.messageInfo_shipNumber_hint)
            MeansOfTransport.TRAIN.toString() -> holder.transportInfoHint.setText(R.string.newGuest_trainNumber_hint)
        }

        holder.guestName.text = guests[position].name
        holder.transportInfo.text = guests[holder.adapterPosition].vehicleInfo
        holder.country.text = guests[holder.adapterPosition].countryOfArrival
        holder.dateAndTimeOfArrival.text =
            holder.itemView.resources.getString(
                R.string.homescreenItem_date_and_time_formatText,
                guests[holder.adapterPosition].dateOfArrival,
                guests[holder.adapterPosition].timeOfArrival
            )
        holder.driver.text = guests[holder.adapterPosition].driverName

        holder.apartmentName.text = guests[position].apartmentName

        holder.transferTypeHint.text = guests[position].transferType

        holder.itemView.setOnClickListener {
            clickListener.onViewClicked(guests[position])
        }

        holder.deleteButton.setOnClickListener {
            clickListener.onTrashCanClicked(guests[position])
        }
    }

    override fun getItemCount(): Int = guests.size

    fun setGuests(guests: List<Guest>) {
        this.guests = guests
        notifyDataSetChanged()
    }

    interface OnItemClicked {
        fun onViewClicked(guest: Guest)
        fun onTrashCanClicked(guest: Guest)
    }
}

class HomeScreenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val transportInfoHint: TextView = itemView.findViewById(R.id.homescreen_transport_info_hint)
    val guestName: TextView = itemView.findViewById(R.id.homescreen_name)
    val transportInfo: TextView = itemView.findViewById(R.id.homescreen_transport_info)
    val country: TextView = itemView.findViewById(R.id.homescreen_city)
    val dateAndTimeOfArrival: TextView = itemView.findViewById(R.id.homescreen_date_and_time)
    val driver: TextView = itemView.findViewById(R.id.homescreen_driver)
    val deleteButton: View = itemView.findViewById(R.id.homescreen_delete_guest)
    val transferTypeHint: TextView = itemView.findViewById(R.id.homescreen_transferType_hint)
    val apartmentName: TextView = itemView.findViewById(R.id.homescreen_apartmentName)
}
