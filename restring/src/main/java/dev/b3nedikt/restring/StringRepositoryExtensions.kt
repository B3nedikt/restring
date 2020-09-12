package dev.b3nedikt.restring

/**
 * Returns a [MutableStringRepository] from this [StringRepository] if possible.
 * If you use a custom implementation of [StringRepository] it needs to implement
 * [MutableStringRepository] for this function to work!
 */
fun StringRepository.toMutableRepository(): MutableStringRepository {
    if (this is MutableStringRepository) {
        return this
    } else {
        error("Your custom repository needs to implement MutableStringsRepository!")
    }
}