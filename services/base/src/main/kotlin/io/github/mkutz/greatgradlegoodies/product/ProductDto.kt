package io.github.mkutz.greatgradlegoodies.product

import io.github.mkutz.greatgradlegoodies.rating.Rating
import java.util.*

data class ProductDto(
  val id: UUID,
  val name: String,
  val description: String = "",
  val ratings: List<Rating> = emptyList()
) {

  constructor(
    product: Product
  ) : this(
    id = product.id,
    name = product.name,
    description = product.description,
    ratings = product.ratings
  )
}
