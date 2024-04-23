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
class ProductApiTest {

  @Value("http://localhost:\${local.server.port}") lateinit var baseUrl: String

  @Autowired lateinit var webClient: WebTestClient
  @Autowired lateinit var ratingRepository: RatingRepository
  @Autowired lateinit var productRepository: ProductRepository

  @Test
  @DisplayName("GET /products/:product-id")
  fun getOne() {
    val entity = productRepository.save(aProduct().buildEntity())
    ratingRepository.saveAll(
      listOf(
        aRating().productId(entity.id).buildEntity(),
        aRating().productId(entity.id).buildEntity()
      )
    )

    val response = webClient.get().uri("$baseUrl/products/${entity.id}").exchange()

    response.expectStatus().isOk().expectBody().jsonPath("$.id").isEqualTo("${entity.id}")
  }
}
