package io.github.mkutz.greatgradlegoodies.rating

import java.time.format.DateTimeFormatter.ISO_INSTANT

data class ReadRatingDto(
  val id: String,
  val productId: String,
  val userName: String,
  val stars: Int,
  val comment: String?,
  val created: String
) {
  constructor(
    rating: Rating
  ) : this(
    id = rating.id.toString(),
    productId = rating.productId.toString(),
    userName = rating.userName,
    stars = rating.stars,
    comment = rating.comment,
    created = ISO_INSTANT.format(rating.created)
  )
}
