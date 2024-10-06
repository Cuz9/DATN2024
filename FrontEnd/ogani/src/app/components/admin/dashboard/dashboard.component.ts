import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {faFaceLaughWink, faTag} from '@fortawesome/free-solid-svg-icons';
import {faSearch} from '@fortawesome/free-solid-svg-icons'
import {faBell} from '@fortawesome/free-solid-svg-icons'
import {faEnvelope} from '@fortawesome/free-solid-svg-icons'
import {faTachometerAlt} from '@fortawesome/free-solid-svg-icons'
import {faBookmark} from '@fortawesome/free-solid-svg-icons'
import {faReceipt} from '@fortawesome/free-solid-svg-icons'
import {faCartShopping} from '@fortawesome/free-solid-svg-icons'
import {faRocket} from '@fortawesome/free-solid-svg-icons'
import {faUser} from '@fortawesome/free-solid-svg-icons'
import {faBars} from '@fortawesome/free-solid-svg-icons'
import {faPaperPlane} from '@fortawesome/free-solid-svg-icons'
import {faGear} from '@fortawesome/free-solid-svg-icons'
import {faRightFromBracket} from '@fortawesome/free-solid-svg-icons'
import {AuthService} from 'src/app/_service/auth.service';
import {StorageService} from 'src/app/_service/storage.service';
import {OrderService} from "../../../_service/order.service";
import {ExportService} from "../../../_service/export.service";


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  faceLaugh = faFaceLaughWink;
  search = faSearch
  bell = faBell;
  evelope = faEnvelope;
  tachometer = faTachometerAlt;
  bookmark = faBookmark;
  receipt = faReceipt;
  cart = faCartShopping;
  rocket = faRocket;
  userIcon = faUser;
  paperPlane = faPaperPlane;
  bars = faBars;
  gear = faGear;
  logoutIcon = faRightFromBracket;
  tag = faTag;
  listOrder: any;
  angular: any;

  constructor(private storageService: StorageService, private authService: AuthService, private orderService: OrderService, private router: Router, private exportService: ExportService) {
  }

  ngOnInit(): void {
    this.getListOrder()
    let excel = document.getElementById("excel")
    if (excel !== null) {
      excel.classList.remove("hide")
      excel.classList.add("show")
    }
  }


  logout() {
    this.authService.logout().subscribe({
      next: res => {
        this.storageService.clean();
        this.router.navigate(['/']);
      }
    })
  }

  getListOrder() {
    this.orderService.getListOrder().subscribe({
      next: res => {
        this.listOrder = res;
        console.log(this.listOrder);
      }, error: err => {
        console.log(err);
      }
    })
  }

  export() {
    this.exportService.exportExcel(this.listOrder, 'listOrder');
  }

  hide() {
    let excel = document.getElementById("excel")
    if (excel !== null) {
      excel.classList.remove("show")
      excel.classList.add("hide")
    }
  }

  show() {
    let excel = document.getElementById("excel")
    if (excel !== null) {
      excel.classList.remove("hide")
      excel.classList.add("show")
    }
  }
}
