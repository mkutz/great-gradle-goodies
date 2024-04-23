package io.github.mkutz.greatgradlegoodies.rating

import jakarta.validation.Validation
import jakarta.validation.Validator
import java.util.UUID.randomUUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class RatingRequestDtoValidatorTest {

  private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

  @TestFactory
  fun valid() =
    listOf(
        "minimal" to CreateRatingDto(productId = "${randomUUID()}", stars = 4, userName = "Alice"),
        "minimum stars" to
          CreateRatingDto(
            productId = "${randomUUID()}",
            stars = 1,
            userName = "Alice",
            comment = "Poor product!"
          ),
        "maximum stars" to
          CreateRatingDto(
            productId = "${randomUUID()}",
            stars = 5,
            userName = "Alice",
            comment = "Excellent product!"
          ),
        "with comment" to
          CreateRatingDto(
            productId = "${randomUUID()}",
            stars = 4,
            userName = "Alice",
            "Good product!"
          )
      )
      .map { (description, dto) ->
        dynamicTest("validate(${description})") { assertThat(validator.validate(dto)).isEmpty() }
      }

  @TestFactory
  fun invalid() =
    listOf(
        "invalid ID" to
          CreateRatingDto(productId = "${randomUUID()}g", stars = 4, userName = "Alice"),
        "blank userName" to
          CreateRatingDto(productId = "${randomUUID()}", stars = 4, userName = ""),
        "too few stars" to CreateRatingDto(productId = "${randomUUID()}", stars = 0, userName = ""),
        "too many stars" to
          CreateRatingDto(productId = "${randomUUID()}", stars = 6, userName = ""),
      )
      .map { (description, dto) ->
        dynamicTest("validate(${description})") { assertThat(validator.validate(dto)).isNotEmpty() }
      }
}
