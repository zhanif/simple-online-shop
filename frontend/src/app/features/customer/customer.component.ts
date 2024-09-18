import { Component, OnInit } from "@angular/core";
import { CustomerService } from "../../services/customer.service";
import { PaginationInfo } from "../../shared/models/pagination-info.model";
import { CommonModule } from "@angular/common";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { faEye, faTrashCan, faPenToSquare } from "@fortawesome/free-regular-svg-icons";
import { ModalCreateCustomerComponent } from "./modal-create-customer/modal-create-customer.component";
import { ModalViewCustomerComponent } from "./modal-view-customer/modal-view-customer.component";

@Component({
  selector: 'app-customer',
  standalone: true,
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.css',
  imports: [
    CommonModule,
    FontAwesomeModule,
    ModalViewCustomerComponent,
    ModalCreateCustomerComponent
  ]
})
export class CustomerComponent implements OnInit {
  customers: any[] = [];
  meta: PaginationInfo = {
    number: 0,
    size: 0,
    totalElements: 0,
    totalPages: 0
  }
  faEye = faEye
  faTrashCan = faTrashCan
  faPenToSquare = faPenToSquare

  selectedCustomerId: number = 0
  isModalViewOpen = false
  isModalCreateOpen = false

  constructor (
    private customerService: CustomerService
  ) {}

  ngOnInit(): void { this.getAll() }
  goToPage(page: number) { this.getAll(page - 1) }
  openModalView(id: number) {
    this.selectedCustomerId = id
    this.isModalViewOpen = true
  }
  openModalCreate() { this.isModalCreateOpen = true }
  closeModalCreate = () => { this.isModalCreateOpen = false }
  closeModalView = () => { this.isModalViewOpen = false }

  private getAll(page: number = 0) {
    this.customerService.getAll(page).subscribe(data => {
      this.customers = data.data
      this.meta = data.meta
    })
  }

  refresh = () => {
    this.closeModalView()
    this.closeModalCreate()

    this.getAll()
  }
}