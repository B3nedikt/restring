package com.b3nedikt.restring

import android.content.Context
import android.content.res.Resources
import android.view.ContextThemeWrapper

/**
 * Main Restring context wrapper which wraps the context for providing another layout inflater
 * & resources.
 */
internal class RestringContextWrapper private constructor(
        base: Context,
        stringRepository: StringRepository
) : ContextThemeWrapper(base, base.applicationInfo.theme) {

    private val res: Resources by lazy {
        val baseResources = super.getResources()
        RestringResources(baseResources, stringRepository)
    }

    override fun getResources() = res

    companion object {

        fun wrap(context: Context,
                 stringRepository: StringRepository): RestringContextWrapper {
            return RestringContextWrapper(context, stringRepository)
        }
    }
}