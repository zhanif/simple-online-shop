import { Component, OnInit } from "@angular/core";
// import { ItemService } from "../../services/customer.service";
import { PaginationInfo } from "../../shared/models/pagination-info.model";
import { CommonModule } from "@angular/common";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { faEye, faTrashCan, faPenToSquare } from "@fortawesome/free-regular-svg-icons";
import { ItemService } from "../../services/item.service";

@Component({
  selector: 'app-item',
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule
  ],
  templateUrl: './item.component.html',
  styleUrl: './item.component.css'
})
export class ItemComponent implements OnInit {
  items: any[] = [];
  meta: PaginationInfo = {
    number: 0,
    size: 0,
    totalElements: 0,
    totalPages: 0
  }
  faEye = faEye
  faTrashCan = faTrashCan
  faPenToSquare = faPenToSquare

  constructor(
    private itemService: ItemService
  ) {}

  ngOnInit(): void { this.getAll(0) }
  
  goToPage(page: number) { this.getAll(page - 1) }

  currencyFormat(number: number) {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'IDR',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(number).replace('IDR', 'Rp.')
  }

  private getAll(page: number) {
    this.itemService.getAll(page).subscribe(data => {
      this.items = data.data
      this.meta = data.meta
    })
  }

}
