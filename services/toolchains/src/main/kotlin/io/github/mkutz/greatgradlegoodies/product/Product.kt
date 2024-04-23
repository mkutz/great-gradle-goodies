package io.github.mkutz.greatgradlegoodies.product

import io.github.mkutz.greatgradlegoodies.rating.Rating
import io.github.mkutz.greatgradlegoodies.rating.RatingEntity
import java.util.*

data class Product(
  val id: UUID,
  val name: String,
  val description: String = "",
  val ratings: List<Rating> = emptyList(),
) {
  constructor(
    entity: ProductEntity,
    ratingEntities: List<RatingEntity>
  ) : this(
    id = entity.id,
    name = entity.name,
    description = entity.description,
    ratings = ratingEntities.map { Rating(it) }
  )
}
