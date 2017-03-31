package creditsuisse

import scala.collection.mutable.ListBuffer

trait Orders {
  /**
    * In memory storage would eventually be persisted to a DB
    */
  protected val orders: ListBuffer[Order]

  private[creditsuisse] def getAll(orderType: OrderType.Value): List[Order] = orders.filter(_.orderType == orderType).toList

  def register(orders: Order*): Unit = this.orders ++= orders
  def cancel(order: Order): Unit = orders -= order
}

object Orders extends Orders {
  override protected val orders: ListBuffer[Order] = ListBuffer()
}
