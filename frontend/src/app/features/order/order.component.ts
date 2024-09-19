import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { OrderResponse } from '../../models/response/order-response.model';
import { PaginationInfo } from '../../shared/models/pagination-info.model';
import { faEye, faPenToSquare, faTrashCan } from '@fortawesome/free-regular-svg-icons';
import { OrderService } from '../../services/order.service';
import { convertToCurrenct, convertToDateTime } from '../../core/utility';
import { ModalViewOrderComponent } from './modal-view-order/modal-view-order.component';
import { ModalCreateOrderComponent } from './modal-create-order/modal-create-order.component';
import { ModalUpdateOrderComponent } from './modal-update-order/modal-update-order.component';
import { ModalDeleteOrderComponent } from './modal-delete-order/modal-delete-order.component';

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule,
    ModalViewOrderComponent,
    ModalCreateOrderComponent,
    ModalUpdateOrderComponent,
    ModalDeleteOrderComponent
  ],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css',
  encapsulation: ViewEncapsulation.None
})
export class OrderComponent implements OnInit {
  orders: Array<OrderResponse> = []
  meta: PaginationInfo = {
    number: 0,
    size: 0,
    totalElements: 0,
    totalPages: 0
  }
  faEye = faEye
  faTrashCan = faTrashCan
  faPenToSquare = faPenToSquare

  selectedId: number = 0
  isModalViewOpen = false
  isModalCreateOpen = false
  isModalUpdateOpen = false
  isModalDeleteOpen = false

  constructor(
    private orderService: OrderService
  ) {}

  ngOnInit(): void { this.getAll() }
  goToPage(page: number) { this.getAll(page - 1) }
  convertToCurrency(number: number | any) { return convertToCurrenct(number) }
  convertToDate(timestamp: number) { return convertToDateTime(timestamp).split(' ')[0] }
  
  openModalView(id: number) {
    this.selectedId = id
    this.isModalViewOpen = true
  }
  closeModalView = () => { this.isModalViewOpen = false }
  openModalCreate() { this.isModalCreateOpen = true }
  closeModalCreate = () => { this.isModalCreateOpen = false }
  openModalUpdate(id: number) {
    this.selectedId = id
    this.isModalUpdateOpen = true
  }
  closeModalUpdate = () => { this.isModalUpdateOpen = false }
  openModalDelete(id: number) {
    this.selectedId = id
    this.isModalDeleteOpen = true
  }
  closeModalDelete = () => { this.isModalDeleteOpen = false }

  private getAll(page: number = 0) {
    this.orderService.getAll(page).subscribe(response => {
      this.orders = response.data
      this.meta = response.meta
    })
  }

  refresh = () => {
    this.closeModalView()
    this.closeModalCreate()
    this.closeModalUpdate()
    this.closeModalDelete()

    this.getAll()
  }

  export() {
    this.orderService.export().subscribe(
      (blob: Blob) => {
        const url = window.URL.createObjectURL(blob)
        const anchor = document.createElement('a')
        anchor.href = url
        anchor.download = this.convertToDate(new Date().getTime()) + '_order-report.pdf'
        anchor.click()

        window.URL.revokeObjectURL(url)
      },
      (error) => { console.error(error) }
    )
  }
}
