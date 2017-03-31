package creditsuisse

trait LiveDashboard {

  protected val orders: Orders

  def displayAllSellOrders: List[LiveOrder] = displayAllOrders(OrderType.SELL, _.pricePerKgInPounds < _.pricePerKgInPounds)
  def displayAllBuyOrders: List[LiveOrder] = displayAllOrders(OrderType.BUY, _.pricePerKgInPounds > _.pricePerKgInPounds)

   /**
    * Most of this logic can be delegated to the DB for performance once we have persistence.
    */
  private def displayAllOrders(orderType: OrderType.Value, orderSortingFn: (LiveOrder, LiveOrder) => Boolean) = {
    orders.getAll(orderType)
      .map(_.liveOrder())
      .groupBy(_.pricePerKgInPounds)
      .map {
        case (pricePerKg, liveOrders) =>
          liveOrders.foldLeft(LiveOrder(quantityInKgs = 0.0, pricePerKg, orderType))((a, liveOrder) => a mergeQuantity liveOrder)
      }.toList
      .sortWith(orderSortingFn)
  }
}

object LiveDashboard extends LiveDashboard {
  override protected val orders: Orders = Orders
}