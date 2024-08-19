import { Component, OnInit } from "@angular/core";
import { NgForm } from "@angular/forms";
import { MessageService } from "primeng/api";


@Component({
    selector: 'app-contact',
    templateUrl: './contact.component.html',
    styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {
    invalidForm: boolean = false;

    constructor(private messageService: MessageService) { }

    contactForm: any = {
        fullname: null,
        email: null,
        phone: null,
        message: null,
    }

    ngOnInit(): void {

    }

    sendEmailMessage() {
        // Kiểm tra trường hợp null
        if (!this.contactForm.fullname || !this.contactForm.email || !this.contactForm.phone || !this.contactForm.message) {
            this.invalidForm = true; // Đánh dấu form là không hợp lệ
            this.showWarn("Vui lòng nhập đầy đủ thông tin!");
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