import { Component, OnInit } from '@angular/core';
import { faBars, faHeart, faInfo, faPhone, faRetweet, faShoppingBag } from '@fortawesome/free-solid-svg-icons';
import { MessageService } from 'primeng/api';
import { CartService } from 'src/app/_service/cart.service';
import { NavbarService } from 'src/app/_service/navbar.service';
import { ProductService } from 'src/app/_service/product.service';
import { WishlistService } from 'src/app/_service/wishlist.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  providers: [MessageService]

})
export class HomeComponent implements OnInit {


  heart = faHeart;
  bag = faShoppingBag;
  retweet = faRetweet;
  info = faInfo;

  listProductNewest: any;
  listProductPrice: any;

  showDepartment = true;


  category_items_response = [

    {
      breakpoint: '1024px',
      numVisible: 3,
      numScroll: 3
    },
    {
      breakpoint: '768px',
      numVisible: 2,
      numScroll: 2
    },
    {
      breakpoint: '560px',
      numVisible: 1,
      numScroll: 1
    }

  ]

  category_items = [
    {
      id: 1,
      src: 'assets/image/cat-1.jpg',
      alt: '',
      title: 'Trái cây'
    },
    {
      id: 2,
      src: 'assets/image/cat-2.jpg',
      alt: '',
      title: 'Đồ khô'
    },
    {
      id: 3,
      src: 'assets/image/cat-3.jpg',
      alt: '',
      title: 'Rau củ'
    },
    {
      id: 4,
      src: 'assets/image/cat-4.jpg',
      alt: '',
      title: 'Nước ép'
    },
    {
      id: 5,
      src: 'assets/image/cat-5.jpg',
      alt: '',
      title: 'Thịt các loại'
    }
  ];

  constructor(
    private productSerive: ProductService, 
    private cartService: CartService, 
    private wishlistService: WishlistService, 
    private messageService: MessageService,
    private navService: NavbarService
  ) { }

  ngOnInit(): void {
    this.getListProduct();
  }

  setActiveItem(item: string) {
    this.navService.activeItem = item;
  }

  getListProduct() {
    this.productSerive.getListProductNewest(8).subscribe({
      next: res => {
        this.listProductNewest = res;
      }, error: err => {
        console.log(err);
      }
    })
    this.productSerive.getListProductByPrice().subscribe({
      next: res => {
        this.listProductPrice = res;
      }, error: err => {
        console.log(err);
      }
    })
  }

  addToCart(item: any) {
    this.cartService.getItems();
    this.showSuccess("Đã thêm vào giỏ hàng!")
    this.cartService.addToCart(item, 1);
  }

  addToWishList(item: any) {
    if (!this.wishlistService.productInWishList(item)) {
      this.showSuccess("Đã vào danh saách yêu thích!")
      this.wishlistService.addToWishList(item);
    }
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
