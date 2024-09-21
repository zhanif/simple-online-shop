import { Component, OnInit } from "@angular/core";
import { CustomerService } from "../../services/customer.service";
import { PaginationInfo } from "../../shared/models/pagination-info.model";
import { CommonModule } from "@angular/common";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { faEye, faTrashCan, faPenToSquare } from "@fortawesome/free-regular-svg-icons";
import { ModalCreateCustomerComponent } from "./modal-create-customer/modal-create-customer.component";
import { ModalViewCustomerComponent } from "./modal-view-customer/modal-view-customer.component";
import { ModalUpdateCustomerComponent } from "./modal-update-customer/modal-update-customer.component";
import { CustomerResponse } from "../../models/response/customer-response.model";
import { ModalDeleteCustomerComponent } from "./modal-delete-customer/modal-delete-customer.component";

@Component({
  selector: 'app-customer',
  standalone: true,
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.css',
  imports: [
    CommonModule,
    FontAwesomeModule,
    ModalViewCustomerComponent,
    ModalCreateCustomerComponent,
    ModalUpdateCustomerComponent,
    ModalDeleteCustomerComponent
  ]
})
export class CustomerComponent implements OnInit {
  customers: Array<CustomerResponse> = [];
  meta: PaginationInfo = {
    number: 0,
    size: 0,
    totalElements: 0,
    totalPages: 0
  }
  faEye = faEye
  faTrashCan = faTrashCan
  faPenToSquare = faPenToSquare
  searchQuery: string = ''
  size = 10

  selectedId: number = 0
  isModalViewOpen = false
  isModalCreateOpen = false
  isModalUpdateOpen = false
  isModalDeleteOpen = false

  constructor (
    private customerService: CustomerService
  ) {}

  ngOnInit(): void { this.getAll() }
  goToPage(page: number) { this.getAll(page - 1) }
  
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
    this.customerService.getAll(page, this.size, this.searchQuery).subscribe(response => {
      this.customers = response.data
      if (response.meta) this.meta = response.meta
    })
  }

  refresh = () => {
    this.closeModalView()
    this.closeModalCreate()
    this.closeModalUpdate()
    this.closeModalDelete()

    this.searchQuery = ''
    this.getAll()
  }

  OnSearch() { this.getAll() }
  searchInput(event: InputEvent | any) { this.searchQuery = event.target.value || '' }
  searchKeyDown(event: KeyboardEvent | any) { if (event.key == 'Enter' || event.keyCode === 13) this.OnSearch() }
}