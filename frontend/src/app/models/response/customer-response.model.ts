export interface CustomerResponse {
  id: number
  name: string,
  address: string,
  code: string,
  phone: string,
  isActive: boolean,
  pic?: string
  createdTime: Date
  lastModifiedTime: Date
}
