package com.b3nedikt.restring.example;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.b3nedikt.restring.Restring;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(Restring.wrapContext(newBase)));
    }
}
