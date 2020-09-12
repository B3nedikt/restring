package dev.b3nedikt.restring.internal

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import dev.b3nedikt.restring.StringRepository

/**
 * Main Restring context wrapper which wraps the context for providing custom resources.
 */
internal class RestringContextWrapper private constructor(
        base: Context,
        stringRepository: StringRepository
) : ContextWrapper(base) {

    private val res: Resources by lazy {
        val baseResources = super.getResources()
        RestringResources(baseResources, stringRepository, this)
    }

    override fun getResources() = res

    companion object {

        fun wrap(context: Context, stringRepository: StringRepository): RestringContextWrapper {
            return RestringContextWrapper(context, stringRepository)
        }
    }
}