package org.mhr.trading.controller



import org.mhr.trading.service.UserService
import org.mhr.trading.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

import java.math.BigDecimal

@RestController
@RequestMapping("/users")
class UserController @Autowired constructor(
    private val userService: UserService
) {

    // Endpoint to add a new user
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun addUser(
        @RequestParam username: String,
        @RequestParam balance: BigDecimal
    ): User {
        return userService.addUser(username, balance)
    }
}
