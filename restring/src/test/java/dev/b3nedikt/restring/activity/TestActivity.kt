package dev.b3nedikt.restring.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.ViewPumpAppCompatDelegate
import dev.b3nedikt.restring.R
import dev.b3nedikt.restring.Restring

class TestActivity : AppCompatActivity() {

    private val appCompatDelegate: AppCompatDelegate by lazy {
        ViewPumpAppCompatDelegate(
                baseDelegate = super.getDelegate(),
                baseContext = this,
                wrapContext = { baseContext -> Restring.wrapContext(baseContext) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AppCompat)
        setContentView(R.layout.test_layout)
    }

    override fun getDelegate(): AppCompatDelegate {
        return appCompatDelegate
    }
}
