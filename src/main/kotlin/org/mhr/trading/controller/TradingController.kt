package org.mhr.trading.controller

import org.mhr.trading.model.Order
import org.mhr.trading.service.TradingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal


@RestController
@RequestMapping("/api/trading")
class TradingController {
    @Autowired
    private val tradingService: TradingService? = null

    // View Account Balance
    @GetMapping("/balance")
    fun getAccountBalance(@RequestParam userId: Long?): BigDecimal {
        return tradingService!!.getBalance(userId!!)
    }

    // Deposit Funds
    @PostMapping("/deposit")
    fun depositFunds(@RequestParam userId: Long?, @RequestParam amount: BigDecimal?) {
        tradingService!!.depositFunds(userId!!, amount)
    }

    // Withdraw Funds
    @PostMapping("/withdraw")
    fun withdrawFunds(@RequestParam userId: Long?, @RequestParam amount: BigDecimal?) {
        tradingService!!.withdrawFunds(userId!!, amount)
    }

    // Funds Transfer Between Accounts
    @PostMapping("/transfer")
    fun transferFunds(
        @RequestParam fromUserId: Long?,
        @RequestParam toUserId: Long?,
        @RequestParam amount: BigDecimal?
    ) {
        tradingService!!.transferFunds(fromUserId!!, toUserId!!, amount)
    }

    // Create Buy Order
    @PostMapping("/placeBuyOrder")
    fun placeBuyOrder(
        @RequestParam userId: Long?, @RequestParam symbol: String?,
        @RequestParam quantity: Int?, @RequestParam price: BigDecimal?
    ): Order {
        return tradingService!!.placeOrder(userId!!, symbol, quantity, price, "buy")
    }

    // Create Sell Order
    @PostMapping("/placeSellOrder")
    fun placeSellOrder(
        @RequestParam userId: Long?, @RequestParam symbol: String?,
        @RequestParam quantity: Int?, @RequestParam price: BigDecimal?
    ): Order {
        return tradingService!!.placeOrder(userId!!, symbol, quantity, price, "sell")
    }

    // View Open Orders
    @GetMapping("/openOrders")
    fun getOpenOrders(@RequestParam userId: Long?): List<Order?>? {
        return tradingService!!.getOpenOrders(userId)
    }

    // Cancel Order
    @DeleteMapping("/cancelOrder")
    fun cancelOrder(@RequestParam orderId: Long?) {
        tradingService!!.cancelOrder(orderId!!)
    }

    // Get Trade History
    @GetMapping("/tradeHistory")
    fun getTradeHistory(@RequestParam userId: Long?): List<Order?>? {
        return tradingService!!.getTradeHistory(userId)
    }

    // View Stock Prices
    @GetMapping("/stockPrice")
    fun getStockPrice(@RequestParam symbol: String?): BigDecimal {
        return tradingService!!.getStockPrice(symbol)
    }

    // Get Stock Market Trends
    @GetMapping("/marketTrends")
    fun getStockMarketTrend(@RequestParam symbol: String?): String {
        return tradingService!!.getStockMarketTrend(symbol)
    }

    // Transaction Fees
    @GetMapping("/transactionFees")
    fun getTransactionFees(@RequestParam tradeAmount: BigDecimal?): BigDecimal {
        return tradingService!!.calculateTransactionFees(tradeAmount!!)
    }
}
