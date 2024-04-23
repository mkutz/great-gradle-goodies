package io.github.mkutz.greatgradlegoodies.product

import io.github.mkutz.greatgradlegoodies.rating.RatingRepository
import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductService(
  private val repository: ProductRepository,
  private val ratingRepository: RatingRepository
) {

  fun getById(id: UUID): Product? =
    repository.findByIdOrNull(id)?.let { Product(it, ratingRepository.findAllByProductId(id)) }
}
