package io.github.mkutz.greatgradlegoodies.rating

import java.time.Instant
import java.util.UUID
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

class RatingTest {

  @Test
  fun `constructor entity`() {
    val entity =
      RatingEntity(
        id = UUID.randomUUID(),
        productId = UUID.randomUUID(),
        userName = "Alice",
        stars = 4,
        comment = "Notes",
        created = Instant.now()
      )

    val rating = Rating(entity)

    assertSoftly {
      it.assertThat(rating.id).isEqualTo(entity.id)
      it.assertThat(rating.productId).isEqualTo(entity.productId)
      it.assertThat(rating.userName).isEqualTo(entity.userName)
      it.assertThat(rating.stars).isEqualTo(entity.stars)
      it.assertThat(rating.comment).isEqualTo(entity.comment)
      it.assertThat(rating.created).isEqualTo(entity.created)
    }
  }
}
