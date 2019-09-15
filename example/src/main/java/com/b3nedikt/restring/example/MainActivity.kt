package com.b3nedikt.restring.example

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Locale.setDefault(Locale.US)

        setContentView(R.layout.activity_main)

        subtitleTextView.text  = getString(R.string.subtitle)
    }
}
