export interface CreateOrderRequest {
  customerId: number
  itemId: number
  quantity: number
  date: Date
}