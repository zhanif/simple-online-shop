export interface ItemResponse {
  id: number
  name: string
  code: string
  stock: number
  price: number
  isAvailable: boolean
  lastReStock: Date
  createdTime: Date
  lastModifiedTime: Date
}