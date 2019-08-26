package com.b3nedikt.restring

/**
 * Contains configuration properties for initializing Restring.
 */
class RestringConfig private constructor(
        val isPersist: Boolean = false,
        val stringsLoader: Restring.StringsLoader? = null) {

    data class Builder(
            private var persist: Boolean = false,
            private var stringsLoader: Restring.StringsLoader? = null) {

        fun persist(persist: Boolean) = apply { this.persist = persist }
        fun stringsLoader(loader: Restring.StringsLoader) = apply { this.stringsLoader = loader }

        fun build() = RestringConfig(persist, stringsLoader)
    }

    companion object {

        internal val default: RestringConfig
            get() = Builder()
                    .persist(true)
                    .build()
    }
}