package org.mhr.trading.repository



import org.mhr.trading.model.UserActivityLog
import org.springframework.data.jpa.repository.JpaRepository

interface UserActivityLogRepository : JpaRepository<UserActivityLog, Long>
