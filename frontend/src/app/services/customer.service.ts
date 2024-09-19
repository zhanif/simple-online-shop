import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CustomerResponse } from '../models/response/customer-response.model';
import { CustomerRequest } from '../models/request/customer-request.model';
import { ListResponse } from '../models/response/list-response.model';
import { SearchOptionResponse } from '../models/response/search-option-response.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'http://localhost:8080/customers'

  constructor(
    private http: HttpClient
  ) {}

  public getAll(page: number = 0, size: number = 10): Observable<ListResponse<CustomerResponse>> {
    return this.http.get<ListResponse<CustomerResponse>>(`${this.apiUrl}?page=${page}&size=${size}`)
  }

  public get(id: number): Observable<CustomerResponse> {
    return this.http.get<CustomerResponse>(`${this.apiUrl}/${id}`)
  }

  public create(request: CustomerRequest | FormData): Observable<any> {
    return this.http.post(this.apiUrl, request)
  }

  public update(id: number, request: CustomerRequest | FormData): Observable<CustomerResponse> {
    return this.http.put<CustomerResponse>(`${this.apiUrl}/${id}`, request)
  }

  public delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`)
  }

  public search(query: string): Observable<Array<SearchOptionResponse>> {
    return this.http.get<Array<SearchOptionResponse>>(`${this.apiUrl}?search=${query}`)
  }
}
