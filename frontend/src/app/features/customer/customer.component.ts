import { Component, OnInit } from "@angular/core";
import { CustomerService } from "../../services/customer.service";
import { CommonModule } from "@angular/common";
import { PaginationInfo } from "../../shared/models/pagination-info.model";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { faEye, faTrashCan, faPenToSquare } from "@fortawesome/free-regular-svg-icons";

@Component({
  selector: 'app-customer',
  standalone: true,
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.css',
  imports: [
    CommonModule,
    FontAwesomeModule
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

  constructor (
    private customerService: CustomerService
  ) {}
  ngOnInit(): void {
    this.getAll(0)
  }

  private getAll(page: number) {
    this.customerService.getAll(page).subscribe(data => {
      this.customers = data.data
      this.meta = data.meta
    })
  }

  goToPage(page: number) {
    this.getAll(page - 1)
  }
}