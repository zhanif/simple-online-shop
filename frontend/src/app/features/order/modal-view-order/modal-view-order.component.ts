import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { OrderResponse } from '../../../models/response/order-response.model';
import { OrderService } from '../../../services/order.service';
import { convertToCurrenct, convertToDateTime } from '../../../core/utility';

@Component({
  selector: 'app-modal-view-order',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './modal-view-order.component.html'
})
export class ModalViewOrderComponent implements OnInit {
  @Input() id!: number
  @Input() closeModalView!: () => void
  order: OrderResponse | any

  constructor(
    private orderService: OrderService
  ) {}

  ngOnInit(): void { this.get() }
  convertToDate(timestamp: number) { return convertToDateTime(timestamp).split(' ')[0] }
  convertToDateTime(timestamp: number) { return convertToDateTime(timestamp) }
  convertToCurrency(number: number) { return convertToCurrenct(number) }

  get() {
    this.orderService.get(this.id).subscribe(
      (response: OrderResponse) => { this.order = response },
      (error) => { console.error(error) }
    )
  }
}
