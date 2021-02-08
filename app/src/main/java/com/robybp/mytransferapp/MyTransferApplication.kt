package com.robybp.mytransferapp

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.robybp.mytransferapp.db.GuestBookDatabase
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RouterImpl
import com.robybp.mytransferapp.navigation.RoutingActionsMediator
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import com.robybp.mytransferapp.screen.dateandtimeofarrival.DateAndTimeViewModel
import com.robybp.mytransferapp.screen.driversmenu.DriversMenuViewModel
import com.robybp.mytransferapp.screen.guestinfo.GuestInfoViewModel
import com.robybp.mytransferapp.screen.home.HomeViewModel
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransportViewModel
import com.robybp.mytransferapp.screen.newguest.airplanebus.NewGuestAirplaneBusViewModel
import com.robybp.mytransferapp.screen.newguest.shiptrain.NewGuestShipTrainViewModel
import com.robybp.mytransferapp.screen.pickdriver.PickDriverViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyTransferApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val application = this
        startKoin {
            modules(createModule(application))
        }
    }

    private fun createModule(application: Application) =
        module {


            single<RoutingActionsSource> { RoutingActionsMediator() }

            factory<Router> { (activity: AppCompatActivity) ->
                RouterImpl(activity.supportFragmentManager)
            }

            single {
                val dao = GuestBookDatabase.getDatabase(application)!!.guestBookDao()
                GuestBookRepository(dao)
            }

            viewModel { HomeViewModel(repository = get(), routingActionsSource = get()) }

            viewModel { DriversMenuViewModel(repository = get(), routingActionsSource = get()) }

            viewModel { NewGuestShipTrainViewModel(repository = get(), routingActionsSource = get()) }

            viewModel { DateAndTimeViewModel() }

            viewModel {
                NewGuestAirplaneBusViewModel(
                    repository = get(),
                    routingActionsSource = get()
                )
            }

            viewModel {
                MeansOfTransportViewModel(
                    routingActionsSource = get()
                )
            }

            viewModel { PickDriverViewModel(repository = get(), routingActionsSource = get()) }

            viewModel { GuestInfoViewModel(repository = get(), routingActionsSource = get()) }
        }
}
