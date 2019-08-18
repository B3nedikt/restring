package com.b3nedikt.restring

/**
 * Contains configuration properties for initializing Restring.
 */
class RestringConfig private constructor() {

    var isPersist: Boolean = false
        private set
    var stringsLoader: Restring.StringsLoader? = null
        private set

    class Builder {
        private var persist: Boolean = false
        private var stringsLoader: Restring.StringsLoader? = null

        fun persist(persist: Boolean): Builder {
            this.persist = persist
            return this
        }

        fun stringsLoader(loader: Restring.StringsLoader): Builder {
            this.stringsLoader = loader
            return this
        }

        fun build(): RestringConfig {
            val config = RestringConfig()
            config.isPersist = persist
            config.stringsLoader = stringsLoader
            return config
        }
    }

    companion object {

        internal val default: RestringConfig
            get() = Builder()
                    .persist(true)
                    .build()
    }
}