import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OrderResponse } from '../models/response/order-response.model';
import { ListResponse } from '../models/response/list-response.model';
import { CreateOrderRequest } from '../models/request/create-order-request.model';
import { UpdateOrderRequest } from '../models/request/update-order-request.model';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8080/orders'

  constructor(
    private http: HttpClient
  ) {}

  public getAll(page: number = 0, size: number = 10): Observable<ListResponse<OrderResponse>> {
    return this.http.get<ListResponse<OrderResponse>>(`${this.apiUrl}?page=${page}&size=${size}`)
  }

  public get(id: number = 0): Observable<OrderResponse> {
    return this.http.get<OrderResponse>(`${this.apiUrl}/${id}`)
  }

  public create(request: CreateOrderRequest): Observable<any> {
    return this.http.post(this.apiUrl, request);
  }

  public update(id: number, request: UpdateOrderRequest): Observable<OrderResponse> {
    return this.http.put<OrderResponse>(`${this.apiUrl}/${id}`, request);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
