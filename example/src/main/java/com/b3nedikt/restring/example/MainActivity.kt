package com.b3nedikt.restring.example

import android.os.Bundle
import com.b3nedikt.restring.RestringLocale
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Swiss german
        RestringLocale.currentLocale = Locale("de", "CH")

        setContentView(R.layout.activity_main)

        titleTextView.text = getText(R.string.title)
    }
}
