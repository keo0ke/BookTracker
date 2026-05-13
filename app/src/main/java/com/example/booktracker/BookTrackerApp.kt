package com.example.booktracker

import android.app.Application
import com.example.booktracker.data.remote.ServiceLocator

class BookTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
    }
}
