package creditsuisse

/**
  * Assumptions
  * @param pricePerKgInPounds is always a whole number
  */
case class Order(userId: String, quantityInKgs: Double, pricePerKgInPounds: Int, orderType: OrderType.Value) {
  def liveOrder() = LiveOrder(quantityInKgs, pricePerKgInPounds, orderType)
}
