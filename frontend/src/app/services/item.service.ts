import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ItemRequest } from '../models/request/item-request.model';
import { ItemResponse } from '../models/response/item-response.model';
import { ListResponse } from '../models/response/list-response.model';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  private apiUrl = 'http://localhost:8080/items'

  constructor(
    private http: HttpClient
  ) {}

  public getAll(page: number = 0, size: number = 10): Observable<ListResponse<ItemResponse>> {
    return this.http.get<ListResponse<ItemResponse>>(`${this.apiUrl}?page=${page}&size=${size}`)
  }

  public get(id: number = 0): Observable<ItemResponse> {
    return this.http.get<ItemResponse>(`${this.apiUrl}/${id}`)
  }

  public create(request: ItemRequest): Observable<any> {
    return this.http.post<ItemResponse>(this.apiUrl, request);
  }

  public update(id: number, request: ItemRequest): Observable<ItemResponse> {
    return this.http.put<ItemResponse>(`${this.apiUrl}/${id}`, request);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
