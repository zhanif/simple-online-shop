import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ListCustomerResponse } from '../models/response/list-customer-response.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'http://localhost:8080/customers'

  constructor(
    private http: HttpClient
  ) {}

  public getAll(page: number, size: number = 10): Observable<ListCustomerResponse> {
    return this.http.get<ListCustomerResponse>(`${this.apiUrl}?page=${page}&size=${size}`)
  }
}
