package com.robybp.mytransferapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.robybp.mytransferapp.screen.home.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            add(R.id.fl_main, HomeFragment())
            commit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onAttachFragment(fragment: androidx.fragment.app.Fragment) {

        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS), 1)
        }

        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 1)
        super.onAttachFragment(fragment)
    }
}
