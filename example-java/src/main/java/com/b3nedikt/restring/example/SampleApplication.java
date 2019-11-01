package com.b3nedikt.restring.example;

import android.app.Application;

import com.b3nedikt.restring.Restring;
import com.b3nedikt.restring.RestringConfig;
import com.b3nedikt.restring.RestringInterceptor;

import io.github.inflationx.viewpump.ViewPump;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Restring.init(this,
                new RestringConfig.Builder()
                        .stringsLoader(new SampleStringsLoader())
                        .build()
        );

        ViewPump.init(ViewPump.builder()
                .addInterceptor(RestringInterceptor.INSTANCE)
                .build()
        );
    }
}