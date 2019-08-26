package com.b3nedikt.restring

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources

/**
 * A context wrapper which provide another Resources instead of the original one.
 */
internal class CustomResourcesContextWrapper(
        base: Context,
        private val resources: Resources
) : ContextWrapper(base) {

    override fun getResources() = resources
}
