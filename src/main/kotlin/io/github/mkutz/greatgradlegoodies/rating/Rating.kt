package io.github.mkutz.greatgradlegoodies.rating

import java.time.Instant
import java.util.*

data class Rating(
  val id: UUID,
  val productId: UUID,
  val userName: String,
  val stars: Int,
  val comment: String?,
  val created: Instant
) {
  constructor(
    entity: RatingEntity
  ) : this(
    id = entity.id,
    productId = entity.productId,
    userName = entity.userName,
    stars = entity.stars,
    comment = entity.comment,
    created = entity.created
  )
}
