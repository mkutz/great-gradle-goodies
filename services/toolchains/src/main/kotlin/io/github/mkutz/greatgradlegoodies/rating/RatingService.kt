package io.github.mkutz.greatgradlegoodies.rating

import java.util.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RatingService(private val repository: RatingRepository) {

  fun getAll() = repository.findAll().map { Rating(it) }

  fun getById(id: UUID) = repository.findByIdOrNull(id)?.let { Rating(it) }

  fun create(rating: Rating) = Rating(repository.save(RatingEntity(rating)))

  fun getByProductId(productId: UUID) = repository.findAllByProductId(productId).map { Rating(it) }
}
