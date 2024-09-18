import { CustomerResponse } from "./customer-response.model"
import { ItemResponse } from "./item-response.model"

export interface OrderResponse {
  id: number
  customerId: number
  itemId: number
  code: string
  totalPrice: string
  quantity: number
  date: Date
  customer: CustomerResponse
  item: ItemResponse
}