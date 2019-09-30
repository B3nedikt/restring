package com.b3nedikt.restring.example

import android.app.Application
import com.b3nedikt.restring.Restring
import com.b3nedikt.restring.RestringConfig

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Restring.init(this,
                RestringConfig.Builder()
                        .persist(true)
                        .stringsLoader(SampleStringsLoader())
                        .build()
        )
    }
}
