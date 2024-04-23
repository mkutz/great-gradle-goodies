package io.github.mkutz.greatgradlegoodies.rating

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Range
import org.hibernate.validator.constraints.UUID

data class CreateRatingDto(
  @get:UUID val productId: String,
  @get:Range(min = 1, max = 5) val stars: Int,
  @get:NotBlank val userName: String,
  val comment: String? = null
)
