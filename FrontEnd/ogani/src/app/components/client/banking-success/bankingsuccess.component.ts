import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { MessageService } from "primeng/api";
import { OrderDetail } from "src/app/_class/order-detail";
import { CartService } from "src/app/_service/cart.service";
import { OrderService } from "src/app/_service/order.service";
import { OrderDataService } from "src/app/_service/orderdata.service";
import { SseService } from "src/app/_service/sse.service";
import { StorageService } from "src/app/_service/storage.service";

@Component({
  selector: 'app-bankingsuccess',
  templateUrl: './bankingsuccess.component.html',
  styleUrls: ['./bankingsucess.component.css'],
  providers: [MessageService]
})

export class BankingSuccessComponent implements OnInit {
  orderId: string = '';
  totalPrice: string = '';
  paymentTime: string = '';
  transactionId: string = '';
  formattedPaymentTime: string = '';
  listOrderDetail: any[] = [];
  orderForm: any;
  username !: string;

  constructor(private storageService: StorageService, private orderService: OrderService, private route: ActivatedRoute, private cartService: CartService) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.orderId = params['orderId'];
      // Nhân totalPrice với 0.01 trước khi gán vào biến
      this.totalPrice = (parseFloat(params['totalPrice']) * 0.01).toFixed(2);
      this.paymentTime = params['paymentTime'];
      this.transactionId = params['transactionId'];
      this.formatPaymentTime(); // Gọi hàm để format paymentTime
    });

    this.username = this.storageService.getUser().username;

    const listOrderDetailString = sessionStorage.getItem('listOrderDetail');
    const orderFormString = sessionStorage.getItem('orderForm');

    if (listOrderDetailString != null) {
      this.listOrderDetail = JSON.parse(listOrderDetailString);
    } else {
      // Xử lý trường hợp không tìm thấy dữ liệu
    }

    if (orderFormString != null) {
      this.orderForm = JSON.parse(orderFormString);
    } else {
      // Xử lý trường hợp không tìm thấy dữ liệu
    }

    console.log(this.listOrderDetail);
    console.log(this.orderForm);
    this.placeOrder();
  }

  // Phương thức để định dạng lại thời gian thanh toán
  formatPaymentTime(): void {
    // Chuyển đổi số thành chuỗi và cắt chuỗi để lấy ngày, tháng, năm
    let paymentTimeString: string = this.paymentTime;
    let day: string = paymentTimeString.substring(6, 8);
    let month: string = paymentTimeString.substring(4, 6);
    let year: string = paymentTimeString.substring(0, 4);

    // Định dạng lại thành 'DD-MM-YYYY'
    this.formattedPaymentTime = `${day}/${month}/${year}`;
  }

  placeOrder() {
    this.orderService.placeOrderByCash(this.orderForm.firstname, this.orderForm.lastname, this.orderForm.country, this.orderForm.address, this.orderForm.town, this.orderForm.state, this.orderForm.postCcode, this.orderForm.phone, this.orderForm.email, this.orderForm.note, this.listOrderDetail, this.username).subscribe({
      next: res => {
        //  Clear giỏ hàng
        this.cartService.clearCart();
      }, error: err => {
        console.log(err);
      }
    })
  }
}