export interface CustomerResponse {
  id: string
  name: string,
  address: string,
  code: string,
  phone: string,
  isActive: boolean,
  pic?: string
  createdTime: Date
  lastModifiedTime: Date
}
