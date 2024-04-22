package io.github.mkutz.greatgradlegoodies.rating

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface RatingRepository : CrudRepository<RatingEntity, UUID> {
  fun findAllByProductId(productId: UUID): List<RatingEntity>
}
