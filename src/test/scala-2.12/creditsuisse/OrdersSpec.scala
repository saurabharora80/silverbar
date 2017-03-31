package creditsuisse

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.ListBuffer

class OrdersSpec extends FlatSpec with Matchers {

  "register" should "register an order" in new TestOrders {
    register(Order("user1", 3.5, 306, OrderType.BUY))

    getAll(OrderType.BUY) should contain only Order("user1", 3.5, 306, OrderType.BUY)
  }

  "register" should "register multiple orders" in new TestOrders {
    register(Order("user1", 3.5, 306, OrderType.BUY),
             Order("user2", 1.2, 310, OrderType.BUY))

    getAll(OrderType.BUY) should contain only (Order("user1", 3.5, 306, OrderType.BUY), Order("user2", 1.2, 310, OrderType.BUY))

  }

  "cancel" should "remove an order" in new TestOrders {
    register(Order("user1", 3.5, 306, OrderType.BUY),
             Order("user2", 1.2, 310, OrderType.BUY))

    cancel(Order("user1", 3.5, 306, OrderType.BUY))

    getAll(OrderType.BUY) should contain only Order("user2", 1.2, 310, OrderType.BUY)

  }

  "listAll" should "return BUY and SELL orders separately" in new TestOrders {
    register(Order("user1", 3.5, 306, OrderType.BUY),
             Order("user2", 1.2, 310, OrderType.BUY),
             Order("user3", 1.5, 307, OrderType.SELL),
             Order("user4", 2.0, 306, OrderType.SELL))

    getAll(OrderType.BUY) should contain only (Order("user1", 3.5, 306, OrderType.BUY),
                                               Order("user2", 1.2, 310, OrderType.BUY))

    getAll(OrderType.SELL) should contain only (Order("user3", 1.5, 307, OrderType.SELL),
                                                Order("user4", 2.0, 306, OrderType.SELL))
  }

  class TestOrders extends Orders {
    override protected val orders: ListBuffer[Order] = ListBuffer()
  }
}
