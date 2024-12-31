package org.mhr.trading.model

import jakarta.persistence.*
import java.math.BigDecimal


@Entity
@Table(name = "users")
data class User (


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     val id: Long = 0,
     val username: String? = null,
     var balance: BigDecimal = BigDecimal.ZERO // Getters and setters
)
