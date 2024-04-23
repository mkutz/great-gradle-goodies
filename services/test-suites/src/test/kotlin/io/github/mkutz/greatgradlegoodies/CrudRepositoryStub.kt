package io.github.mkutz.greatgradlegoodies

import java.util.*
import org.springframework.data.repository.CrudRepository

abstract class CrudRepositoryStub<T, ID : Any> : CrudRepository<T, ID> {

  protected val data = HashMap<ID, T>()

  @Suppress("UNCHECKED_CAST")
  override fun findById(id: ID) = Optional.ofNullable<T>(data[id]) as Optional<T>

  override fun findAll() = data.values

  override fun deleteById(id: ID) {
    data.remove(id)
  }

  override fun existsById(id: ID) = data.containsKey(id)

  override fun findAllById(ids: Iterable<ID>) = data.filterKeys { ids.contains(it) }.values

  override fun count() = data.count().toLong()

  override fun delete(entity: T & Any) {
    data.values.remove(entity)
  }

  override fun deleteAllById(ids: Iterable<ID>) {
    data.keys.removeAll(ids)
  }

  override fun deleteAll(entities: Iterable<T>) {
    data.values.removeAll(entities)
  }

  override fun deleteAll() {
    data.clear()
  }
}
