package io.github.mkutz.greatgradlegoodies.rating

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

data class RatingBuilder(
  private var id: UUID = UUID.randomUUID(),
  private var productId: UUID = UUID.randomUUID(),
  private var userName: String = "Alice",
  private var stars: Int = 5,
  private var comment: String = "Amazing product!",
  private var created: Instant = Instant.now().minus(1, ChronoUnit.DAYS)
) {

  fun id(id: UUID) = apply { this.id = id }

  fun productId(productId: UUID) = apply { this.productId = productId }

  fun stars(stars: Int) = apply { this.stars = stars }

  fun comment(comment: String) = apply { this.comment = comment }

  fun created(created: Instant) = apply { this.created = created }

  fun build(): Rating =
    Rating(
      id = id,
      productId = productId,
      userName = userName,
      comment = comment,
      stars = stars,
      created = created
    )

  fun buildEntity() = RatingEntity(build())

  fun buildCreateDto() =
    CreateRatingDto(
      productId = productId.toString(),
      stars = stars,
      userName = userName,
      comment = comment
    )
}

fun aRating() = RatingBuilder()
