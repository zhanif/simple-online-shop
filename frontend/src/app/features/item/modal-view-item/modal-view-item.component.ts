import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { ItemResponse } from '../../../models/response/item-response.model';
import { ItemService } from '../../../services/item.service';
import { convertToCurrenct, convertToDateTime } from '../../../core/utility';

@Component({
  selector: 'app-modal-view-item',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './modal-view-item.component.html'
})
export class ModalViewItemComponent implements OnInit {
  @Input() id!: number
  @Input() closeModalView!: () => void
  
  item: ItemResponse | any

  constructor(
    private itemService: ItemService
  ) {}

  ngOnInit(): void { this.get() }
  convertToDate(timestamp: number) { return convertToDateTime(timestamp).split(' ')[0] }
  convertToDateTime(timestamp: number) { return convertToDateTime(timestamp) }
  convertToCurrency(number: number) { return convertToCurrenct(number) }

  get() {
    this.itemService.get(this.id).subscribe(
      (response: ItemResponse) => { this.item = response },
      (error) => { console.error(error) }
    )
  }
}
