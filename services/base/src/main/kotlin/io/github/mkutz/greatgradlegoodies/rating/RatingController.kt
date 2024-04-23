package io.github.mkutz.greatgradlegoodies.rating

import jakarta.validation.Valid
import java.time.Instant
import java.util.*
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
class RatingController(private val service: RatingService) {

  @GetMapping(path = ["/products/{productId}/ratings"], produces = ["application/json"])
  fun getAll(@PathVariable productId: String): ResponseEntity<List<ReadRatingDto>> {
    return ok(service.getByProductId(UUID.fromString(productId)).map { ReadRatingDto(it) })
  }

  @GetMapping(path = ["/products/{productId}/ratings/{id}"], produces = ["application/json"])
  fun getOne(@PathVariable id: String): ResponseEntity<ReadRatingDto> {
    val rating = service.getById(UUID.fromString(id))
    return rating?.let { ok(ReadRatingDto(it)) } ?: notFound().build()
  }

  @PostMapping(
    path = ["/products/{productId}/ratings"],
    consumes = ["application/json"],
    produces = ["application/json"]
  )
  fun post(
    @PathVariable productId: String,
    @Valid @RequestBody dto: CreateRatingDto,
    uriComponentsBuilder: UriComponentsBuilder
  ): ResponseEntity<ReadRatingDto> {
    val createdRating =
      service.create(
        Rating(
          id = UUID.randomUUID(),
          productId = UUID.fromString(dto.productId),
          userName = dto.userName,
          stars = dto.stars,
          comment = dto.comment,
          created = Instant.now()
        )
      )
    return created(
        uriComponentsBuilder
          .path("/products/{productId}/ratings/{id}")
          .buildAndExpand(productId, createdRating.id)
          .toUri()
      )
      .body(ReadRatingDto(createdRating))
  }
}
