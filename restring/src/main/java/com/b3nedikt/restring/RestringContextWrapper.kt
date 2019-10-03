package com.b3nedikt.restring

import android.content.Context
import android.content.res.Resources
import android.view.ContextThemeWrapper
import android.view.LayoutInflater

/**
 * Main Restring context wrapper which wraps the context for providing another layout inflater
 * & resources.
 */
internal class RestringContextWrapper private constructor(
        base: Context,
        stringRepository: StringRepository,
        private val viewTransformerManager: ViewTransformerManager
) : ContextThemeWrapper(base, base.applicationInfo.theme) {

    private val res: Resources by lazy {
        val baseResources = super.getResources()
        RestringResources(baseResources, stringRepository)
    }

    private val layoutInflater: RestringLayoutInflater by lazy {
        RestringLayoutInflater(
                original = LayoutInflater.from(baseContext),
                newContext = this,
                viewTransformerManager = viewTransformerManager,
                cloned = true
        )
    }

    override fun getSystemService(name: String): Any? {
        if (Context.LAYOUT_INFLATER_SERVICE == name) {
            return layoutInflater
        }

        return super.getSystemService(name)
    }

    override fun getResources() = res

    companion object {

        fun wrap(context: Context,
                 stringRepository: StringRepository,
                 viewTransformerManager: ViewTransformerManager): RestringContextWrapper {
            return RestringContextWrapper(context, stringRepository, viewTransformerManager)
        }
    }
}