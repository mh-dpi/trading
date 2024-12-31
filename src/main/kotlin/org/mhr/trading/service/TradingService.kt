package org.mhr.trading.service

import org.mhr.trading.model.Order
import org.mhr.trading.repository.OrderRepository
import org.mhr.trading.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TradingService {
    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val orderRepository: OrderRepository? = null

    // View Account Balance
    fun getBalance(userId: Long): BigDecimal {
        val user = userRepository!!.findById(userId).orElseThrow {
            RuntimeException(
                "User not found"
            )
        }!!
        return user.getBalance()
    }

    // Deposit Funds
    fun depositFunds(userId: Long, amount: BigDecimal?) {
        val user = userRepository!!.findById(userId).orElseThrow {
            RuntimeException(
                "User not found"
            )
        }!!
        user.setBalance(user.getBalance().add(amount))
        userRepository.save(user)
    }

    // Withdraw Funds
    fun withdrawFunds(userId: Long, amount: BigDecimal?) {
        val user = userRepository!!.findById(userId).orElseThrow {
            RuntimeException(
                "User not found"
            )
        }!!
        if (user.getBalance().compareTo(amount) < 0) {
            throw RuntimeException("Insufficient funds")
        }
        user.setBalance(user.getBalance().subtract(amount))
        userRepository.save(user)
    }

    // Transfer Funds Between Accounts
    fun transferFunds(fromUserId: Long, toUserId: Long, amount: BigDecimal?) {
        val fromUser = userRepository!!.findById(fromUserId).orElseThrow {
            RuntimeException(
                "Sender not found"
            )
        }!!
        val toUser = userRepository.findById(toUserId).orElseThrow {
            RuntimeException(
                "Receiver not found"
            )
        }!!

        if (fromUser.getBalance().compareTo(amount) < 0) {
            throw RuntimeException("Insufficient funds")
        }

        fromUser.setBalance(fromUser.getBalance().subtract(amount))
        toUser.setBalance(toUser.getBalance().add(amount))

        userRepository.save(fromUser)
        userRepository.save(toUser)
    }

    // Place Order (Buy or Sell)
    fun placeOrder(userId: Long, symbol: String?, quantity: Int?, price: BigDecimal?, orderType: String?): Order {
        val user = userRepository!!.findById(userId).orElseThrow {
            RuntimeException(
                "User not found"
            )
        }!!
        val order: Order = Order()
        order.setUser(user)
        order.setSymbol(symbol)
        order.setQuantity(quantity)
        order.setPrice(price)
        order.setOrderType(orderType)
        order.setStatus("pending")
        order.setTimestamp(LocalDateTime.now())
        return orderRepository!!.save(order)
    }

    // View Open Orders
    fun getOpenOrders(userId: Long?): List<Order?>? {
        return orderRepository!!.findByUserIdAndStatus(userId, "pending")
    }

    // Cancel Order
    fun cancelOrder(orderId: Long) {
        val order: Order = orderRepository!!.findById(orderId).orElseThrow {
            RuntimeException(
                "Order not found"
            )
        }
        if (!order.getStatus().equals("pending")) {
            throw RuntimeException("Cannot cancel completed or cancelled orders")
        }
        order.setStatus("cancelled")
        orderRepository.save<Order>(order)
    }

    // Get Trade History
    fun getTradeHistory(userId: Long?): List<Order?>? {
        return orderRepository!!.findByUserIdAndStatus(userId, "completed")
    }

    // View Stock Prices (Mocked for this example)
    fun getStockPrice(symbol: String?): BigDecimal {
        // In a real application, fetch the live stock price from an external API
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
