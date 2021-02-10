package com.robybp.mytransferapp.navigation

import androidx.fragment.app.FragmentTransaction
import com.robybp.mytransferapp.R

fun FragmentTransaction.applySlideAnimation() {
    setCustomAnimations(
        R.anim.fragment_slide_in,
        R.anim.fragment_fade_exit,
        R.anim.fragment_fade_enter,
        R.anim.fragment_slide_out
    )
}