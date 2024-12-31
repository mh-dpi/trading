package org.mhr.trading.model


import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_activity_log")
data class UserActivityLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    val activityType: String, // e.g., "CREATED", "UPDATED", "DELETED"
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val description: String? = null // Additional description or context
)
