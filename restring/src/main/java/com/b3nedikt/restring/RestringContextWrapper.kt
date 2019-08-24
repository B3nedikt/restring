package com.b3nedikt.restring

import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater

/**
 * Main Restring context wrapper which wraps the context for providing another layout inflater & resources.
 */
internal class RestringContextWrapper private constructor(
        base: Context,
        stringRepository: StringRepository,
        private val viewTransformerManager: ViewTransformerManager
) : ContextWrapper(CustomResourcesContextWrapper(base, RestringResources(base.resources, stringRepository))) {

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

    companion object {

        fun wrap(context: Context,
                 stringRepository: StringRepository,
                 viewTransformerManager: ViewTransformerManager): RestringContextWrapper {
            return RestringContextWrapper(context, stringRepository, viewTransformerManager)
        }
    }
}