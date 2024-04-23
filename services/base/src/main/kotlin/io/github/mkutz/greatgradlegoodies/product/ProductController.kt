package io.github.mkutz.greatgradlegoodies.product

import java.util.*
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(private val service: ProductService) {

  @GetMapping(path = ["/products/{productId}"], produces = ["application/json"])
  fun getOne(@PathVariable productId: String) =
    service.getById(UUID.fromString(productId))?.let { ok(ProductDto(it)) } ?: notFound().build()
}
