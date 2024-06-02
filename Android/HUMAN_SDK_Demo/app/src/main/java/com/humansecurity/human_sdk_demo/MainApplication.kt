package com.humansecurity.human_sdk_demo

import android.app.Application

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        HumanManager.start(this)
    }
}
