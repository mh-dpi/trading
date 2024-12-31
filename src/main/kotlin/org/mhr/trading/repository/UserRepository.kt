package org.mhr.trading.repository

import org.mhr.trading.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


interface UserRepository : JpaRepository<User?, Long?> {
    fun findByUsername(username: String?): User?
}


