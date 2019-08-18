package com.b3nedikt.restring

import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater

/**
 * Main Restring context wrapper which wraps the context for providing another layout inflater & resources.
 */
internal class RestringContextWrapper private constructor(base: Context,
                                                          stringRepository: StringRepository,
                                                          private val viewTransformerManager: ViewTransformerManager) : ContextWrapper(CustomResourcesContextWrapper(
        base,
        RestringResources(base.resources, stringRepository))) {

    private var layoutInflater: RestringLayoutInflater? = null

    override fun getSystemService(name: String): Any? {
        if (Context.LAYOUT_INFLATER_SERVICE == name) {
            if (layoutInflater == null) {
                layoutInflater = RestringLayoutInflater(LayoutInflater.from(baseContext), this, viewTransformerManager, true)
            }
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