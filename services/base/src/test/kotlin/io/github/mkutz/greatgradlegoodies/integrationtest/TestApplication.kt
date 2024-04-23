package io.github.mkutz.greatgradlegoodies.integrationtest

import io.github.mkutz.greatgradlegoodies.Application
import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName.parse

@TestConfiguration(proxyBeanMethods = false)
@Testcontainers
class TestApplication {

  @Bean
  @ServiceConnection
  fun postgreSQLContainer() =
    PostgreSQLContainer(parse("postgres:16").asCompatibleSubstituteFor("postgres"))
}

fun main(args: Array<String>) {
  fromApplication<Application>().with(TestApplication::class).run(*args)
}
