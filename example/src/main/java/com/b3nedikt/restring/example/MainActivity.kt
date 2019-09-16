package com.b3nedikt.restring.example

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Swiss german
        Locale.setDefault(Locale("de", "CH"))

        setContentView(R.layout.activity_main)
    }
}
