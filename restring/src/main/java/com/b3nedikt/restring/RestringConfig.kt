package com.b3nedikt.restring

import android.view.View

/**
 * Contains configuration properties for initializing Restring.
 */
class RestringConfig private constructor(
        internal val stringRepository: StringRepository? = null,
        internal val viewTransformers: Set<ViewTransformer<View>> = emptySet(),
        internal val stringsLoader: Restring.StringsLoader? = null) {

    data class Builder(
            private var stringRepository: StringRepository? = null,
            private var viewTransformers: Set<ViewTransformer<View>> = mutableSetOf(),
            private var stringsLoader: Restring.StringsLoader? = null) {

        fun stringRepository(stringRepository: StringRepository) = apply { this.stringRepository = stringRepository }
        fun stringsLoader(loader: Restring.StringsLoader) = apply { this.stringsLoader = loader }
        fun addViewTransformer(vararg viewTransformer: ViewTransformer<*>) = apply { this.viewTransformers.plus(viewTransformer) }

        fun build() = RestringConfig(stringRepository, viewTransformers, stringsLoader)
    }

    companion object {

        internal val default: RestringConfig
            get() = Builder().build()
    }
}