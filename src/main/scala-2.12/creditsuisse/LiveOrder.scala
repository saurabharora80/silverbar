package creditsuisse

case class LiveOrder(quantityInKgs: Double, pricePerKgInPounds: Int, buyOrSell: OrderType.Value) {
  def mergeQuantity(order: LiveOrder): LiveOrder = LiveOrder(this.quantityInKgs + order.quantityInKgs, this.pricePerKgInPounds, this.buyOrSell)
}
