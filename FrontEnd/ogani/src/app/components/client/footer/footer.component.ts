import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {
  invalidForm: boolean = false;
  email: string = 'phanlananh454@gmail.com';
  clientEmail: string = '';
  usefulLinks: string[] = [
    'Về chúng tôi',
    'Bảo mật thông tin mua hàng',
    'Thông tin giao hàng',
    'Chính sách bảo mật',
    'Dịch vụ',
    'Liên hệ',
  ];
  newsletterTitle: string = 'Tham gia cùng chúng tôi';
  newsletterDescription: string = 'Thông báo về thông tin khuyến mãi và mặt hàng mới';
  subscribeButton: string = 'Đăng ký';

  constructor(private messageService: MessageService) { }

  ngOnInit(): void {
  }

  sendEmailRegister() {
    if (!this.clientEmail) {
      this.invalidForm = true; // Đánh dấu form là không hợp lệ
      this.showWarn("Vui lòng nhập email!");
      return; // Dừng hàm nếu form không hợp lệ
    }

    this.showSuccess("Gửi tin nhắn thành công!");
  }

  showSuccess(text: string) {
    this.messageService.add({ severity: 'success', summary: 'Thành công', detail: text });
  }

  showError(text: string) {
    this.messageService.add({ severity: 'error', summary: 'Thất bại', detail: text });
  }

  showWarn(text: string) {
    this.messageService.add({ severity: 'warn', summary: 'Cảnh báo', detail: text });
  }
}
