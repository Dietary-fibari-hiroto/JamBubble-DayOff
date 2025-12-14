package com.example.jambubble_client


import android.app.Application
import com.example.jambubble_client.data.UserLocalDataStore

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        UserLocalDataStore.initialize(this)
    }
}