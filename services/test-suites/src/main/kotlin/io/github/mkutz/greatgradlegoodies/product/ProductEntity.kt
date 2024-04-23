package io.github.mkutz.greatgradlegoodies.product

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "product")
data class ProductEntity(@Id val id: UUID, val name: String, val description: String = "") {
  constructor(bo: Product) : this(bo.id, bo.name, bo.description)
}
