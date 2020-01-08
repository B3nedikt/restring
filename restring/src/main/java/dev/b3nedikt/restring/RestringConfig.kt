package dev.b3nedikt.restring

/**
 * Contains configuration properties for initializing Restring.
 */
class RestringConfig private constructor(
        internal val stringRepository: StringRepository? = null,
        internal val stringsLoader: Restring.StringsLoader? = null,
        internal val loadAsync: Boolean = true) {

    data class Builder(
            private var stringRepository: StringRepository? = null,
            private var stringsLoader: Restring.StringsLoader? = null,
            private var loadAsync: Boolean = true) {

        fun stringRepository(stringRepository: StringRepository) = apply { this.stringRepository = stringRepository }
        fun stringsLoader(loader: Restring.StringsLoader) = apply { this.stringsLoader = loader }
        fun loadAsync(loadAsync: Boolean) = apply { this.loadAsync = loadAsync }

        fun build() = RestringConfig(stringRepository, stringsLoader, loadAsync)
    }

    companion object {

        internal val default: RestringConfig
            get() = Builder().build()
    }
}