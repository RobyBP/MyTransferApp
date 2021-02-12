package com.robybp.mytransferapp.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransport
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel(
    private val repository: GuestBookRepository,
    private val routingActionsSource: RoutingActionsSource
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val guests: Flowable<List<Guest>> = repository.allGuests
        .subscribeOn(Schedulers.io())
        .map { list ->
            list.filter { guest ->
                LocalDate.now()
                    .isEqual(LocalDate.parse(guest.dateOfArrival, formatter)) || LocalDate.now()
                    .isBefore(LocalDate.parse(guest.dateOfArrival, formatter))
            }
        }
        .observeOn(AndroidSchedulers.mainThread())

    init {
        compositeDisposable.add(
            repository.allGuests
                .subscribeOn(Schedulers.io())
                .map { list ->
                    list.filter { guest ->
                        LocalDate.now()
                            .isAfter(LocalDate.parse(guest.dateOfArrival, formatter))
                    }
                }
                .doOnNext { deleteGuests(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
        compositeDisposable.dispose()
    }

    private fun deleteGuests(guests: List<Guest>) = viewModelScope.launch(Dispatchers.IO) {
        for (guest in guests) {
            repository.removeGuest(guest)
        }
    }

    fun goToMeansOfTransport() = routingActionsSource.dispatch(Router::goToMeansOfTransport)

    fun goToDriversMenu() = routingActionsSource.dispatch(Router::goToDriversMenu)

    fun goToGuestInfo(guest: Guest) {
        when (guest.meansOfTransport) {
            MeansOfTransport.BUS.toString() -> routingActionsSource.dispatch {
                it.goToGuestInfoBus(
                    guest.guestId
                )
            }
            MeansOfTransport.AIRPLANE.toString() -> routingActionsSource.dispatch {
                it.goToGuestInfoPlane(
                    guest.guestId
                )
            }
            MeansOfTransport.SHIP.toString() -> routingActionsSource.dispatch {
                it.goToGuestInfoShip(
                    guest.guestId
                )
            }
            MeansOfTransport.TRAIN.toString() -> routingActionsSource.dispatch {
                it.goToGuestInfoTrain(
                    guest.guestId
                )
            }
        }
    }
}
