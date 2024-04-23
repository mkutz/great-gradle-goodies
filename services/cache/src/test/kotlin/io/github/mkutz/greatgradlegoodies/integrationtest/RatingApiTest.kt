package io.github.mkutz.greatgradlegoodies.integrationtest

import io.github.mkutz.greatgradlegoodies.product.ProductRepository
import io.github.mkutz.greatgradlegoodies.product.aProduct
import io.github.mkutz.greatgradlegoodies.rating.RatingRepository
import io.github.mkutz.greatgradlegoodies.rating.aRating
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = [TestApplication::class])
class RatingApiTest {

  @Value("http://localhost:\${local.server.port}") lateinit var baseUrl: String

  @Autowired lateinit var webClient: WebTestClient
  @Autowired lateinit var ratingRepository: RatingRepository
  @Autowired lateinit var productRepository: ProductRepository

  @Test
  @DisplayName("GET /products/:product-id/ratings")
  fun getAll() {
    val product = productRepository.save(aProduct().buildEntity())
    val entity = ratingRepository.save(aRating().productId(product.id).buildEntity())

    val response = webClient.get().uri("$baseUrl/products/${product.id}/ratings").exchange()

    response.expectStatus().isOk().expectBody().jsonPath("$[0].id").isEqualTo("${entity.id}")
  }

  @Test
  @DisplayName("GET /products/:product-id/ratings/:id")
  fun getOne() {
    val product = productRepository.save(aProduct().buildEntity())
    val entity = ratingRepository.save(aRating().buildEntity())

    val response =
      webClient.get().uri("$baseUrl/products/${product.id}/ratings/${entity.id}").exchange()

    response.expectAll(
      { it.expectStatus().isOk() },
      { it.expectBody().jsonPath("$.id").isEqualTo("${entity.id}") }
    )
  }

  @Test
  @DisplayName("POST /products/:product-id/ratings")
  fun post() {
    val product = productRepository.save(aProduct().buildEntity())
    val body = aRating().buildCreateDto()

    val response =
      webClient
        .post()
        .uri("$baseUrl/products/${product.id}/ratings")
        .header("Content-Type", "application/json")
        .bodyValue(body)
        .exchange()

    response.expectAll(
      { it.expectStatus().isCreated() },
      { it.expectHeader().valueMatches("Location", "$baseUrl/products/${product.id}/ratings/.+") }
    )
  }

  @Test
  @DisplayName("POST /products/:product-id/ratings invalid")
  fun postInvalid() {
    val product = productRepository.save(aProduct().buildEntity())

    val response =
      webClient
        .post()
        .uri("$baseUrl/products/${product.id}/ratings")
        .header("Content-Type", "application/json")
        .bodyValue("""{"name":"","pros":"Simple\nlayered"}""")
        .exchange()

    response.expectAll(
      { it.expectStatus().isBadRequest() },
      { it.expectHeader().doesNotExist("Location") },
      { it.expectBody().jsonPath("title", "Bad Request") },
      { it.expectBody().jsonPath("detail", "Invalid request content.") },
      { it.expectBody().jsonPath("status", "400") }
    )
  }
}
