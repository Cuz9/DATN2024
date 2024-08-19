import { Component, OnInit } from "@angular/core";
import { MessageService } from "primeng/api";

@Component({
    selector: 'app-bankingcancel',
    templateUrl: './bankingcancel.component.html',
    styleUrls: ['./bankingcancel.component.css'],
    providers: [MessageService]
})

export class BankingCancelComponent implements OnInit {
    ngOnInit(): void {
        
    }
}