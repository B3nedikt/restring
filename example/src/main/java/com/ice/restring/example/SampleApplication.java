package com.ice.restring.example;

import android.app.Application;

import com.ice.restring.Restring;
import com.ice.restring.RestringConfig;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Restring.init(this,
                new RestringConfig.Builder()
                        .persist(true)
                        .stringsLoader(new SampleStringsLoader())
                        .build()
        );
    }
}
