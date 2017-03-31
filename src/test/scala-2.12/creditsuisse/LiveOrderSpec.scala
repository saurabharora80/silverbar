package creditsuisse

import org.scalatest.{FlatSpec, Matchers}

class LiveOrderSpec extends FlatSpec with Matchers {

  "mergeQuantity" should "only merge the quantity of Live Orders" in {
    LiveOrder(quantityInKgs = 3.3, pricePerKgInPounds = 300, OrderType.SELL) mergeQuantity
      LiveOrder(quantityInKgs = 3.3, pricePerKgInPounds = 400, OrderType.BUY) shouldBe
      LiveOrder(quantityInKgs = 6.6, pricePerKgInPounds = 300, OrderType.SELL)

    LiveOrder(quantityInKgs = 4.3, pricePerKgInPounds = 300, OrderType.BUY) mergeQuantity
      LiveOrder(quantityInKgs = 3.3, pricePerKgInPounds = 400, OrderType.SELL) shouldBe
      LiveOrder(quantityInKgs = 7.6, pricePerKgInPounds = 300, OrderType.BUY)
  }
}
