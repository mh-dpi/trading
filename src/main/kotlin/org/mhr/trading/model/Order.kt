package org.mhr.trading.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Table(name = "orders")
data class Order (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var symbol: String? = null,
    var quantity: Int? = null,
    var price: BigDecimal? = null,
    var orderType: String? = null, // "buy" or "sell"
    var status: String? = null,   // "pending", "completed", "cancelled"
    var timestamp: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")  // Corrected column name here
    var user: User? = null
)
