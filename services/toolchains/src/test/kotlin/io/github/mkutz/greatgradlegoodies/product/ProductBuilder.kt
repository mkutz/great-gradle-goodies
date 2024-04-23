package io.github.mkutz.greatgradlegoodies.product

import io.github.mkutz.greatgradlegoodies.rating.Rating
import java.util.*

data class ProductBuilder(
  private var id: UUID = UUID.randomUUID(),
  private var name: String = "REWE Highle Welt Schokocookies",
  private var description: String = "",
  private var ratings: List<Rating> = emptyList(),
) {

  fun id(id: UUID) = apply { this.id = id }

  fun name(name: String) = apply { this.name = name }

  fun description(description: String) = apply { this.description = description }

  fun ratings(ratings: List<Rating>) = apply { this.ratings = ratings }

  fun build(): Product = Product(id = id, name = name, description = description, ratings = ratings)

  fun buildEntity() = ProductEntity(build())
}

fun aProduct() = ProductBuilder()
