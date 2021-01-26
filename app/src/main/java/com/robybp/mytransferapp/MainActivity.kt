package com.robybp.mytransferapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.robybp.mytransferapp.fragments.homescreen.HomeScreenFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_main, HomeScreenFragment())
            commit()
        }
    }
}