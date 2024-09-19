import { Component, OnInit } from "@angular/core";
import { PaginationInfo } from "../../shared/models/pagination-info.model";
import { CommonModule } from "@angular/common";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { faEye, faTrashCan, faPenToSquare } from "@fortawesome/free-regular-svg-icons";
import { ItemService } from "../../services/item.service";
import { ItemResponse } from "../../models/response/item-response.model";
import { convertToCurrenct } from "../../core/utility";
import { ModalViewItemComponent } from "./modal-view-item/modal-view-item.component";
import { ModalCreateItemComponent } from "./modal-create-item/modal-create-item.component";
import { ModalUpdateItemComponent } from "./modal-update-item/modal-update-item.component";
import { ModalDeleteItemComponent } from "./modal-delete-item/modal-delete-item.component";
import { ModalDeleteCustomerComponent } from "../customer/modal-delete-customer/modal-delete-customer.component";

@Component({
  selector: 'app-item',
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule,
    ModalViewItemComponent,
    ModalCreateItemComponent,
    ModalUpdateItemComponent,
    ModalDeleteItemComponent,
    ModalDeleteCustomerComponent
],
  templateUrl: './item.component.html',
  styleUrl: './item.component.css'
})
export class ItemComponent implements OnInit {
  items: Array<ItemResponse> = [];
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
    private itemService: ItemService
  ) {}

  ngOnInit(): void { this.getAll() }
  goToPage(page: number) { this.getAll(page - 1) }
  convertToCurrency(number: number) { return convertToCurrenct(number) }

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
    this.itemService.getAll(page).subscribe(response => {
      this.items = response.data
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
}
