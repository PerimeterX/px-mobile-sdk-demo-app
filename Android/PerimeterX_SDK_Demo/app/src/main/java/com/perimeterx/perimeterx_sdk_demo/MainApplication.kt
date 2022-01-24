package com.perimeterx.android_sdk_demo

import android.app.Application

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        PerimeterxManager.start(this)
    }
}
