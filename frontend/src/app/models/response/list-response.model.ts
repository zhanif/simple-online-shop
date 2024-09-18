import { PaginationInfo } from "../../shared/models/pagination-info.model";

export interface ListResponse<T> {
  data: T[]
  meta: PaginationInfo
}