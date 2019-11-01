package com.b3nedikt.restring.example

import android.app.Application
import com.b3nedikt.restring.Restring
import com.b3nedikt.restring.RestringConfig
import com.b3nedikt.restring.RestringInterceptor
import io.github.inflationx.viewpump.ViewPump


class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Restring.init(this,
                RestringConfig.Builder()
                        .stringsLoader(SampleStringsLoader())
                        .loadAsync(true)
                        .build()
        )

        ViewPump.init(ViewPump.builder()
                .addInterceptor(RestringInterceptor)
                .build()
        )
    }
}