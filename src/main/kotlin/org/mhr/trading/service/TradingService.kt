package org.mhr.trading.service

import org.mhr.trading.model.Order
import org.mhr.trading.model.UserActivityLog
import org.mhr.trading.repository.OrderRepository
import org.mhr.trading.repository.UserRepository
import org.mhr.trading.repository.UserActivityLogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class TradingService {

    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val orderRepository: OrderRepository? = null

    @Autowired
    private val userActivityLogRepository: UserActivityLogRepository? = null

    // Log User Activity
    private fun logUserActivity(userId: Long, activityType: String, description: String) {
        val user = userRepository!!.findById(userId).orElseThrow {
            RuntimeException("User not found")
        }
        val activityLog = UserActivityLog(
            user = user!!,
            activityType = activityType,
            description = description,
            timestamp = LocalDateTime.now()
        )
        userActivityLogRepository!!.save(activityLog)
    }

    // View Account Balance
    fun getBalance(userId: Long): BigDecimal {
        val user = userRepository!!.findById(userId).orElseThrow {
            RuntimeException("User not found")
        }
        logUserActivity(userId, "VIEW_BALANCE", "Viewed balance")
        if (user != null) {
            return user.balance
        }
        return BigDecimal.ZERO
    }

    // Deposit Funds
    fun depositFunds(userId: Long, amount: BigDecimal?) {
        val user = userRepository!!.findById(userId).orElseThrow {
            RuntimeException("User not found")
        }
        if (user != null) {
            user.balance = user.balance.add(amount)
        }
        if (user != null) {
            userRepository.save(user)
        }
        logUserActivity(userId, "DEPOSIT", "Deposited $amount")
    }

    // Withdraw Funds
    fun withdrawFunds(userId: Long, amount: BigDecimal?) {
        val user = userRepository!!.findById(userId).orElseThrow {
            RuntimeException("User not found")
        }
        if (user != null) {
            if (user.balance.compareTo(amount) < 0) {
                throw RuntimeException("Insufficient funds")
            }
        }
        if (user != null) {
            user.balance = amount?.let { user.balance.minus(it) }!!
        }
        if (user != null) {
            userRepository.save(user)
        }
        logUserActivity(userId, "WITHDRAW", "Withdrew $amount")
    }

    // Transfer Funds Between Accounts
    fun transferFunds(fromUserId: Long, toUserId: Long, amount: BigDecimal?) {
        val fromUser = userRepository!!.findById(fromUserId).orElseThrow {
            RuntimeException("Sender not found")
        }
        val toUser = userRepository!!.findById(toUserId).orElseThrow {
            RuntimeException("Receiver not found")
        }

        if (fromUser != null) {
            if (fromUser.balance.compareTo(amount) < 0) {
                throw RuntimeException("Insufficient funds")
            }
        }

        if (fromUser != null) {
            fromUser.balance = fromUser.balance.subtract(amount)
        }
        if (toUser != null) {
            toUser.balance = toUser.balance.add(amount)
        }

        if (fromUser != null) {
            userRepository.save(fromUser)
        }
        if (toUser != null) {
            userRepository.save(toUser)
        }

        logUserActivity(fromUserId, "TRANSFER", "Transferred $amount to user ID: $toUserId")
        logUserActivity(toUserId, "TRANSFER", "Received $amount from user ID: $fromUserId")
    }

    // Place Order (Buy or Sell)
    fun placeOrder(userId: Long, symbol: String?, quantity: Int?, price: BigDecimal?, orderType: String?): Order {
        val user = userRepository!!.findById(userId).orElseThrow {
            RuntimeException("User not found")
        }
        val order = Order()
        order.user = user
        order.symbol = symbol
        order.quantity = quantity
        order.price = price
        order.orderType = orderType
        order.status = "pending"
        order.timestamp = LocalDateTime.now()

        val savedOrder = orderRepository!!.save(order)
        logUserActivity(userId, "PLACE_ORDER", "Placed order for $quantity of $symbol at price $price")
        return savedOrder
    }

    // View Open Orders
    fun getOpenOrders(userId: Long?): List<Order?>? {
        val openOrders = orderRepository!!.findByUserIdAndStatus(userId, "pending")
        logUserActivity(userId!!, "VIEW_OPEN_ORDERS", "Viewed open orders")
        return openOrders
    }

    // Cancel Order
    fun cancelOrder(orderId: Long) {
        val order = orderRepository!!.findById(orderId).orElseThrow {
            RuntimeException("Order not found")
        }
        if (order != null) {
            if (order.status != "pending") {
                throw RuntimeException("Cannot cancel completed or cancelled orders")
            }
        }
        if (order != null) {
            order.status = "cancelled"
        }
        if (order != null) {
            orderRepository.save(order)
        }
        if (order != null) {
            order.user?.let { logUserActivity(it.id, "CANCEL_ORDER", "Cancelled order ID: $orderId") }
        }
    }

    // Get Trade History
    fun getTradeHistory(userId: Long?): List<Order?>? {
        val tradeHistory = orderRepository!!.findByUserIdAndStatus(userId, "completed")
        logUserActivity(userId!!, "VIEW_TRADE_HISTORY", "Viewed trade history")
        return tradeHistory
    }

    // View Stock Prices (Mocked for this example)
    fun getStockPrice(symbol: String?): BigDecimal {
        return BigDecimal("100.00") // Placeholder value
    }

    // Get Stock Market Trends (Mocked for this example)
    fun getStockMarketTrend(symbol: String?): String {
        return "Stock is trending upwards" // Placeholder trend
    }

    // Transaction Fees
    fun calculateTransactionFees(tradeAmount: BigDecimal): BigDecimal {
        val feeRate = BigDecimal("0.002") // 0.2% fee rate
        return tradeAmount.multiply(feeRate)
    }
}
