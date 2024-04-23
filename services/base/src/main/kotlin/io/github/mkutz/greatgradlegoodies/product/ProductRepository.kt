package io.github.mkutz.greatgradlegoodies.product

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface ProductRepository : CrudRepository<ProductEntity, UUID>
