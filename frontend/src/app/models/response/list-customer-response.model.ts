import { PaginationInfo } from "../../shared/models/pagination-info.model";
import { CustomerResponse } from "./customer-request.model";

export interface ListCustomerResponse {
  data: CustomerResponse[]
  meta: PaginationInfo
}