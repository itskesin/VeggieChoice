package com.veggiechoice.orbital.viewmodel

import androidx.lifecycle.ViewModel
import com.veggiechoice.orbital.Filters

/**
 * ViewModel for [com.google.firebase.example.fireeats.MainActivity].
 */

class MainActivityViewModel : ViewModel() {

    var isSigningIn: Boolean = false
    var filters: Filters = Filters.default
}
