import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NavbarService {
  activeItem: string = 'home'; // Khởi tạo mục đang active

  constructor() { }
}
