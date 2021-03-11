package com.robybp.mytransferapp

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkManager
import com.robybp.mytransferapp.db.GuestBookDatabase
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RouterImpl
import com.robybp.mytransferapp.navigation.RoutingActionsMediator
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import com.robybp.mytransferapp.notificationscheduler.ScheduleNotificationUseCase
import com.robybp.mytransferapp.screen.apartmentinfo.ApartmentInfoViewModel
import com.robybp.mytransferapp.screen.apartmentsmenu.ApartmentsMenuViewModel
import com.robybp.mytransferapp.screen.dateandtimeofarrival.DateAndTimeViewModel
import com.robybp.mytransferapp.screen.driversmenu.DriversMenuViewModel
import com.robybp.mytransferapp.screen.guestinfo.GuestInfoViewModel
import com.robybp.mytransferapp.screen.home.HomeViewModel
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransportViewModel
import com.robybp.mytransferapp.screen.newapartment.NewApartmentViewModel
import com.robybp.mytransferapp.screen.newguest.NewGuestViewModel
import com.robybp.mytransferapp.screen.pickapartment.PickApartmentViewModel
import com.robybp.mytransferapp.screen.pickdriver.PickDriverViewModel
import com.robybp.mytransferapp.screen.transfertype.TransferTypeViewModel
import com.robybp.mytransferapp.sms.FindPhoneNumberUseCase
import com.robybp.mytransferapp.sms.FormatMessageUseCase
import com.robybp.mytransferapp.sms.SendSmsUseCase
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

            factory { WorkManager.getInstance(application.applicationContext) }

            single { FindPhoneNumberUseCase(repository = get()) }

            single { FormatMessageUseCase(context = applicationContext) }

            single { SendSmsUseCase() }

            single { ScheduleNotificationUseCase(applicationContext) }

            viewModel { HomeViewModel(repository = get(), routingActionsSource = get()) }

            viewModel {
                NewGuestViewModel(
                    repository = get(),
                    routingActionsSource = get(),
                    findPhoneNumberUseCase = get(),
                    formatMessageUseCase = get(),
                    sendSmsUseCase = get(),
                    scheduleNotificationUseCase = get()
                )
            }

            viewModel { DriversMenuViewModel(repository = get(), routingActionsSource = get()) }

            viewModel { DateAndTimeViewModel() }

            viewModel {
                MeansOfTransportViewModel(
                    routingActionsSource = get()
                )
            }

            viewModel { PickDriverViewModel(repository = get(), routingActionsSource = get()) }

            viewModel { GuestInfoViewModel(repository = get(), routingActionsSource = get()) }

            viewModel { ApartmentsMenuViewModel(repository = get(), routingActionsSource = get()) }

            viewModel { NewApartmentViewModel(repository = get(), routingActionsSource = get()) }

            viewModel { TransferTypeViewModel(routingActionsSource = get()) }

            viewModel { ApartmentInfoViewModel(repository = get(), routingActionsSource = get()) }

            viewModel { PickApartmentViewModel(repository = get(), routingActionsSource = get()) }
        }
}
