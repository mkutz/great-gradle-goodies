package io.github.mkutz.greatgradlegoodies.rating

import io.github.mkutz.greatgradlegoodies.product.aProduct
import java.time.format.DateTimeFormatter.ISO_INSTANT
import java.util.UUID.randomUUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.util.UriComponentsBuilder

class RatingControllerTest {

  private val product = aProduct().buildEntity()
  private val repository = RatingRepositoryStub()

  private val controller = RatingController(RatingService(repository))

  @Test
  fun getAll() {
    val entities =
      repository
        .saveAll(
          listOf(
            aRating().productId(product.id).buildEntity(),
            aRating().productId(product.id).buildEntity(),
            aRating().productId(product.id).buildEntity()
          )
        )
        .toList()

    val response = controller.getAll(productId = product.id.toString())

    assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(response.body).hasSize(entities.size)
    assertThat(response.body?.map { it.id }).containsAll(entities.map { it.id.toString() })
  }

  @Test
  fun `getAll empty`() {
    val response = controller.getAll(randomUUID().toString())

    assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(response.body).isEmpty()
  }

  @Test
  fun getOne() {
    val entity = repository.save(aRating().buildEntity())

    val response = controller.getOne(entity.id.toString())

    assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(response.body)
      .hasFieldOrPropertyWithValue("id", entity.id.toString())
      .hasFieldOrPropertyWithValue("productId", entity.productId.toString())
      .hasFieldOrPropertyWithValue("userName", entity.userName)
      .hasFieldOrPropertyWithValue("stars", entity.stars)
      .hasFieldOrPropertyWithValue("comment", entity.comment)
      .hasFieldOrPropertyWithValue("created", ISO_INSTANT.format(entity.created))
  }

  @Test
  fun `getOne unknown`() {
    val unknownId = randomUUID().toString()

    val response = controller.getOne(unknownId)

    assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
  }

  @Test
  fun post() {
    val requestBody = aRating().buildCreateDto()

    val response =
      controller.post(
        productId = product.id.toString(),
        dto = requestBody,
        uriComponentsBuilder = UriComponentsBuilder.newInstance()
      )

    assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
    assertThat(response.body?.id).isNotNull()
    assertThat(response.headers.location?.path).endsWith(response.body?.id)
    assertThat(response.body)
      .hasFieldOrPropertyWithValue("productId", requestBody.productId)
      .hasFieldOrPropertyWithValue("stars", requestBody.stars)
      .hasFieldOrPropertyWithValue("userName", requestBody.userName)
      .hasFieldOrPropertyWithValue("comment", requestBody.comment)
      .hasNoNullFieldsOrProperties()
  }
}
