package com.b3nedikt.restring.example

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

import com.b3nedikt.restring.Restring

/**
 * We should wrap the base context of our activities, which is better to put it
 * on BaseActivity, so that we don't have to repeat it for all activities one-by-one.
 */
open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(Restring.wrapContext(newBase))
    }
}
