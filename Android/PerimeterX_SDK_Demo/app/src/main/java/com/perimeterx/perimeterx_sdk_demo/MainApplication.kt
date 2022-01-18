package com.perimeterx.perimeterx_sdk_demo

import android.app.Application

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        PerimeterxManager.start(this)
    }
}
