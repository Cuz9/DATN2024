import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Order } from '../_class/order';
import { OrderDetail } from '../_class/order-detail';

const ORDER_API = "http://localhost:8080/api/order/";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'Accept': 'application/json' // Thêm headers Accept
  })
};


@Injectable({
  providedIn: 'root'
})

export class OrderService {
  constructor(private http: HttpClient) { }

  getListOrder(): Observable<any> {
    return this.http.get(ORDER_API, httpOptions);
  }

  getListOrderByUser(username: string): Observable<any> {
    let params = new HttpParams();
    params = params.append('username', username);
    return this.http.get(ORDER_API + 'user', { params: params });

  }

  placeOrderByCash(firstname: string, lastname: string, country: string, address: string, town: string, state: string, postCode: string, phone: string, email: string, note: string, orderDetails: OrderDetail[], username: string): Observable<any> {
    return this.http.post(ORDER_API + 'create', { firstname, lastname, country, address, town, state, postCode, phone, email, note, orderDetails, username }, httpOptions);
  }

  placeOrderInternetBanking(firstname: string, lastname: string, country: string, address: string, town: string, state: string, postCode: string, phone: string, email: string, note: string, orderDetails: OrderDetail[], username: string): Observable<any> {
    return this.http.post(ORDER_API + 'submit-order-vnpay', { firstname, lastname, country, address, town, state, postCode, phone, email, note, orderDetails, username }, httpOptions);
  }

  placeOrder(firstname: string, lastname: string, country: string, address: string, town: string, state: string, postCode: string, phone: string, email: string, note: string, orderDetails: OrderDetail[], username: string): Observable<any>{
    return this.http.post(ORDER_API + 'create', { firstname, lastname, country, address, town, state, postCode, phone, email, note, orderDetails, username }, httpOptions);
  }

  createPaymentLinkInternetBanking(firstname: string, lastname: string, country: string, address: string, town: string, state: string, postCode: string, phone: string, email: string, note: string, description: string, returnUrl: string, cancelUrl: string, productList: OrderDetail[]): Observable<any> {
    // Tạo đối tượng chứa dữ liệu
    const requestBody = {
      description: description,
      returnUrl: returnUrl,
      cancelUrl: cancelUrl,
      productList: productList
    };

    // Gửi yêu cầu tạo liên kết thanh toán đến backend
    return this.http.post(ORDER_API + 'create-payment-link', { firstname, lastname, country, address, town, state, postCode, phone, email, note, productList, description, returnUrl, cancelUrl }, httpOptions);
  }
}
