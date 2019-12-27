package dev.b3nedikt.restring.example;

import android.content.Context;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import dev.b3nedikt.restring.Restring;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(Restring.wrapContext(newBase)));
    }

    @Override
    public Resources getResources() {
        return getBaseContext().getResources();
    }
}
