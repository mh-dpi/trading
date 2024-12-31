package org.mhr.trading.repository

import org.mhr.trading.model.Order
import org.springframework.data.jpa.repository.JpaRepository


interface OrderRepository : JpaRepository<Order?, Long?> {
    fun findByUserIdAndStatus(userId: Long?, status: String?): List<Order?>?
}
