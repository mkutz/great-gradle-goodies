package io.github.mkutz.greatgradlegoodies.rating

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Entity
@Table(name = "ratings")
data class RatingEntity(
  @Id val id: UUID,
  val productId: UUID,
  val userName: String,
  val stars: Int,
  val comment: String?,
  val created: Instant,
) {
  constructor(
    bo: Rating
  ) : this(
    id = bo.id,
    productId = bo.productId,
    userName = bo.userName,
    stars = bo.stars,
    comment = bo.comment,
    created = bo.created,
  )
}
