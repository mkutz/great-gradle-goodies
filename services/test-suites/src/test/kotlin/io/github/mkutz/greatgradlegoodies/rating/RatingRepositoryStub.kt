package io.github.mkutz.greatgradlegoodies.rating

import io.github.mkutz.greatgradlegoodies.CrudRepositoryStub
import java.util.*

class RatingRepositoryStub : CrudRepositoryStub<RatingEntity, UUID>(), RatingRepository {

  override fun findAllByProductId(productId: UUID): List<RatingEntity> {
    return data.values.filter { it.productId == productId }
  }

  override fun <S : RatingEntity?> save(entity: S & Any): S & Any {
    return entity.also { data[entity.id] = entity }
  }

  override fun <S : RatingEntity> saveAll(entities: Iterable<S>): Iterable<S> {
    return entities.also { data.putAll(entities.associateBy { it.id }) }
  }
}
