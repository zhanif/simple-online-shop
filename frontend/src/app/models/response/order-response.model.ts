import { CustomerResponse } from "./customer-response.model"
import { ItemResponse } from "./item-response.model"

export interface OrderResponse {
  id: number
  customerId: number
  itemId: number
  code: string
  totalPrice: number
  quantity: number
  date: number
  customer: CustomerResponse
  item: ItemResponse
  createdTime: Date
  lastModifiedTime: Date
}