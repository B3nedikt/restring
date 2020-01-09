package dev.b3nedikt.restring.example

import android.app.Application
import dev.b3nedikt.restring.Restring
import dev.b3nedikt.restring.RestringConfig
import dev.b3nedikt.reword.RewordInterceptor
import io.github.inflationx.viewpump.ViewPump


class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Restring.init(this,
                RestringConfig.Builder()
                        .stringsLoader(SampleStringsLoader())
                        .build()
        )

        ViewPump.init(ViewPump.builder()
                .addInterceptor(RewordInterceptor)
                .build()
        )
    }
}