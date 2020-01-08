package dev.b3nedikt.restring.example;

import android.app.Application;

import dev.b3nedikt.restring.Restring;
import dev.b3nedikt.restring.RestringConfig;
import dev.b3nedikt.reword.RewordInterceptor;
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
                .addInterceptor(RewordInterceptor.INSTANCE)
                .build()
        );
    }
}