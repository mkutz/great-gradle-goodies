package io.github.mkutz.greatgradlegoodies.rating

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RatingServiceTest {

  private val repository = RatingRepositoryStub()

  private val service = RatingService(repository)

  @Test
  fun `getAll empty`() {
    assertThat(service.getAll()).isEmpty()
  }
}
