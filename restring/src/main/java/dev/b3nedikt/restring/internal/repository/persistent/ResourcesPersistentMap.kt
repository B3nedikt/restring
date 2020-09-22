package dev.b3nedikt.restring.internal.repository.persistent

/**
 * [PersistentMap] with string resource names as keys and the string resources as value
 */
class ResourcesPersistentMap<T>(
        private val keyValueStore: KeyValueStore<String, T>
) : PersistentMap<String, T> {

    override fun find(key: String): T? {
        return keyValueStore.find(key)
    }

    override fun findAll(): Map<out String, T> {
        return keyValueStore.findAll()
    }

    override fun save(key: String, value: T) {
        keyValueStore.save(key, value)
    }

    override fun saveAll(from: Map<out String, T>) {
        keyValueStore.saveAll(from)
    }

    override fun delete(key: String) {
        keyValueStore.delete(key)
    }

    override fun deleteAll() {
        keyValueStore.deleteAll()
    }
}