package creditsuisse

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.ListBuffer

class LiveDashboardSpec extends FlatSpec with Matchers {

  "live dashboard" should "display SELL orders in ascending order" in {
    val orders = new TestOrders()

    orders.register(Order("user1", 3.5, 306, OrderType.SELL),
                    Order("user2", 1.2, 310, OrderType.SELL),
                    Order("user3", 1.5, 307, OrderType.SELL))

    new TestLiveDashboard(orders).displayAllSellOrders should contain inOrder(LiveOrder(3.5, 306, OrderType.SELL),
                                                                              LiveOrder(1.5, 307, OrderType.SELL),
                                                                              LiveOrder(1.2, 310, OrderType.SELL))
  }

  "live dashboard" should "display all SELL orders after combining by price and ordered by ascending price" in {
    val orders = new TestOrders()
    orders.register(Order("user1", 3.5, 306, OrderType.SELL),
                    Order("user2", 1.2, 310, OrderType.SELL),
                    Order("user3", 1.5, 307, OrderType.SELL),
                    Order("user4", 2.0, 306, OrderType.SELL))

    new TestLiveDashboard(orders).displayAllSellOrders should contain inOrder(LiveOrder(5.5, 306, OrderType.SELL),
                                                                              LiveOrder(1.5, 307, OrderType.SELL),
                                                                              LiveOrder(1.2, 310, OrderType.SELL))
  }

  "live dashboard" should "display all BUY orders after combining by price and ordered by descending price" in {
    val orders = new TestOrders()

    orders.register(Order("user1", 3.5, 306, OrderType.BUY),
                    Order("user2", 1.2, 310, OrderType.BUY),
                    Order("user3", 1.5, 307, OrderType.BUY),
                    Order("user4", 2.0, 306, OrderType.BUY))

    new TestLiveDashboard(orders).displayAllBuyOrders should contain inOrder(LiveOrder(1.2, 310, OrderType.BUY),
                                                                             LiveOrder(1.5, 307, OrderType.BUY),
                                                                             LiveOrder(5.5, 306, OrderType.BUY))
  }

  "live dashboard" should "display all BUY & SELL orders separately" in {
    val orders = new TestOrders()
    orders.register(Order("user1", 3.5, 306, OrderType.BUY),
                    Order("user2", 1.2, 310, OrderType.SELL),
                    Order("user3", 1.5, 307, OrderType.BUY),
                    Order("user4", 2.0, 306, OrderType.SELL))

    val liveDashboard = new TestLiveDashboard(orders)

    liveDashboard.displayAllBuyOrders should contain inOrder(LiveOrder(1.5, 307, OrderType.BUY),
                                                             LiveOrder(3.5, 306, OrderType.BUY))

    liveDashboard.displayAllSellOrders should contain inOrder(LiveOrder(2.0, 306, OrderType.SELL),
                                                              LiveOrder(1.2, 310, OrderType.SELL))
  }


  "live dashboard" should "not display cancelled order" in {
    val orders = new TestOrders()

    orders.register(Order("user1", 3.5, 306, OrderType.BUY),
                    Order("user2", 1.2, 310, OrderType.SELL),
                    Order("user3", 1.5, 307, OrderType.BUY),
                    Order("user4", 2.0, 306, OrderType.SELL))

    orders.cancel(Order("user3", 1.5, 307, OrderType.BUY))

    val liveDashboard = new TestLiveDashboard(orders)

    liveDashboard.displayAllBuyOrders should contain only LiveOrder(3.5, 306, OrderType.BUY)

    liveDashboard.displayAllSellOrders should contain inOrder(LiveOrder(2.0, 306, OrderType.SELL),
                                                              LiveOrder(1.2, 310, OrderType.SELL))
  }

  "live dashboard" should "display nothing when all orders have been cancelled" in {
    val orders = new TestOrders()

    orders.register(Order("user1", 3.5, 306, OrderType.BUY),
                    Order("user3", 1.5, 307, OrderType.BUY))

    orders.cancel(Order("user3", 1.5, 307, OrderType.BUY))
    orders.cancel(Order("user1", 3.5, 306, OrderType.BUY))

    val liveDashboard = new TestLiveDashboard(orders)

    liveDashboard.displayAllBuyOrders shouldBe empty
    liveDashboard.displayAllSellOrders shouldBe empty
  }

  "live dashboard" should "display nothing when there are no registered orders" in {
    val liveDashboard = new TestLiveDashboard(new TestOrders())

    liveDashboard.displayAllBuyOrders shouldBe empty
    liveDashboard.displayAllSellOrders shouldBe empty
  }

  class TestLiveDashboard(override val orders: Orders) extends LiveDashboard

  class TestOrders(override val orders: ListBuffer[Order] = ListBuffer()) extends Orders
}
