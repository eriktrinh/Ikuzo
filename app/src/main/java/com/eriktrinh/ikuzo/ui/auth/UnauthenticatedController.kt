package com.eriktrinh.ikuzo.ui.auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.eriktrinh.ikuzo.R

class UnauthenticatedController : Controller() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_unauthenticated_view, container, false)
    }
}

