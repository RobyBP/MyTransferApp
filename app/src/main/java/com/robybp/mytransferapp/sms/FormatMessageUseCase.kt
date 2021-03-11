package com.robybp.mytransferapp.sms

import android.content.Context
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.datamodels.Apartment
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransport

class FormatMessageUseCase(private val context: Context) {

    fun formatMessage(guest: Guest): String {
        return if (guest.meansOfTransport == MeansOfTransport.AIRPLANE.toString() || guest.meansOfTransport == MeansOfTransport.BUS.toString()) airplaneBusMessageFormat(
            guest
        ) else shipTrainMessageFormat(guest)
    }

    fun formatMessage(apartment: Apartment): String {

        var note = ""

        if (!apartment.note.isNullOrBlank()) note = "\n" + apartment.note

        return context.resources.getString(
            R.string.apartmentInfo_messageBody,
            context.resources.getString(R.string.apartment_name),
            apartment.name,
            context.resources.getString(R.string.apartment_city_hint),
            apartment.city,
            context.resources.getString(R.string.address),
            apartment.address,
            context.resources.getString(R.string.owner),
            apartment.owner,
            context.resources.getString(R.string.phone),
            apartment.ownerPhoneNumber,
            note
        )
    }

    private fun airplaneBusMessageFormat(guest: Guest): String {
        val flightNuderOrBusCompany =
            if (guest.meansOfTransport == MeansOfTransport.BUS.toString()) context.resources.getString(R.string.messageInfo_busCompany_hint) else context.resources.getString(
                R.string.messageInfo_flightNumber_hint
            )

        return context.resources.getString(
            R.string.airplaneOrBus_messageBody,
            context.resources.getString(R.string.messageInfo_guestName_hint),
            guest.name,
            flightNuderOrBusCompany,
            guest.vehicleInfo,
            context.resources.getString(R.string.messageInfo_arrival_hint),
            guest.countryOfArrival,
            context.resources.getString(R.string.messageInfo_dateAndTimeOfArrival),
            guest.dateOfArrival,
            guest.timeOfArrival,
            guest.transferType,
            guest.apartmentName
        )
    }

    private fun shipTrainMessageFormat(guest: Guest): String {
        val shipOrTrainNumber =
            if (guest.meansOfTransport == MeansOfTransport.SHIP.toString()) context.resources.getString(R.string.messageInfo_shipNumber_hint) else context.resources.getString(
                R.string.messageInfo_trainNumber_hint
            )

        val portOrStation =
            if (guest.meansOfTransport == MeansOfTransport.SHIP.toString()) context.resources.getString(R.string.newGuest_shipOnPort_hint) else context.resources.getString(
                R.string.newGuest_trainOnStation_hint
            )

        return context.resources.getString(
            R.string.shipOrTrain_messageBody,
            context.resources.getString(R.string.messageInfo_guestName_hint),
            guest.name,
            shipOrTrainNumber,
            guest.vehicleInfo,
            portOrStation,
            guest.portOrStation,
            context.resources.getString(R.string.messageInfo_arrival_hint),
            guest.countryOfArrival,
            context.resources.getString(R.string.messageInfo_dateAndTimeOfArrival),
            guest.dateOfArrival,
            guest.timeOfArrival,
            guest.transferType,
            guest.apartmentName
        )
    }
}
