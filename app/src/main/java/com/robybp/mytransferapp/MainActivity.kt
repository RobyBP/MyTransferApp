package com.robybp.mytransferapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionConsumer
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity(), RoutingActionConsumer {

    private val router: Router by inject(parameters = { parametersOf(this) })

    private val mediator: RoutingActionsSource by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) mediator.dispatch(Router::goToHomeScreen)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onAttachFragment(fragment: androidx.fragment.app.Fragment) {

        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS), 1)
        }

        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 1)
        super.onAttachFragment(fragment)
    }

    override fun onRoutingAction(routingAction: (Router) -> Unit) = routingAction(router)

    override fun onStart() {
        super.onStart()
        mediator.registerActiveConsumer(this)
    }

    override fun onStop() {
        mediator.deregisterConsumer()
        super.onStop()
    }
}
