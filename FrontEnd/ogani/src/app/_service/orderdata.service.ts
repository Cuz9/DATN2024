// order-data.service.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class OrderDataService {
  listOrderDetail: any[] = [];
  orderForm: any;

  constructor() { }
}
