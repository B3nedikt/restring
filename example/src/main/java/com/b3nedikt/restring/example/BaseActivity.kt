package com.b3nedikt.restring.example

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

import com.b3nedikt.restring.Restring
import io.github.inflationx.viewpump.ViewPumpContextWrapper

abstract class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(Restring.wrapContext(newBase)))
    }
}
