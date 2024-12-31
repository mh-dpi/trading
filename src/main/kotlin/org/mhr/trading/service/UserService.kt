package org.mhr.trading.service

import org.mhr.trading.model.User
import org.mhr.trading.model.UserActivityLog
import org.mhr.trading.repository.UserRepository
import org.mhr.trading.repository.UserActivityLogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private val userActivityLogRepository: UserActivityLogRepository
) {
    // Logger for the service
    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    // Method to add a new user
    fun addUser(username: String, balance: BigDecimal): User {
        logger.info("Attempting to create a new user with username: $username and balance: $balance")
        val newUser = User(username = username, balance = balance)
        val savedUser = userRepository.save(newUser)

        // Log the activity
        logUserActivity(savedUser, "CREATED", "Created a new user with username: $username and balance: $balance")

        logger.info("Successfully created a new user with ID: ${savedUser.id}")
        return savedUser
    }

    // Method to update user balance
    fun updateUserBalance(userId: Long, newBalance: BigDecimal): User? {
        logger.info("Attempting to update balance for user with ID: $userId")
        val user = userRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val oldBalance = user?.balance
        if (user != null) {
            user.balance = newBalance
        }
        val updatedUser = user?.let { userRepository.save(it) }

        // Log the activity
        if (updatedUser != null) {
            logUserActivity(updatedUser, "UPDATED", "Updated balance from $oldBalance to $newBalance")
        }

        logger.info("Successfully updated balance for user with ID: $userId to: $newBalance")
        return updatedUser
    }

    // Method to fetch user by ID
    fun getUserById(userId: Long): User? {
        logger.info("Fetching user with ID: $userId")
        val user = userRepository.findById(userId).orElseThrow {
            logger.error("User with ID: $userId not found!")
            RuntimeException("User not found")
        }

        // Log the activity
        if (user != null) {
            logUserActivity(user, "FETCHED", "Fetched user with ID: $userId")
        }

        if (user != null) {
            logger.info("Returning user with ID: ${user.id}")
        }
        return user
    }

    // Helper method to log user activity
    private fun logUserActivity(user: User, activityType: String, description: String) {
        val activityLog = UserActivityLog(
            user = user,
            activityType = activityType,
            description = description,
            timestamp = LocalDateTime.now()
        )
        userActivityLogRepository.save(activityLog)
    }
}
