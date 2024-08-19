import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SseService {
    constructor(private http: HttpClient) { }

    listenForEvents(): Observable<any> {
        const headers = new HttpHeaders().set('Content-Type', 'text/event-stream');
        return this.http.get<any>('http://localhost:8080/api/vnpay/vnpay-payment-stream', { headers });
    }
}

