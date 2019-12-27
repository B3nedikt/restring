package dev.b3nedikt.restring.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import dev.b3nedikt.restring.R
import dev.b3nedikt.restring.Restring
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class TestActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(Restring.wrapContext(newBase)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AppCompat)
        setContentView(R.layout.test_layout)
    }
}
