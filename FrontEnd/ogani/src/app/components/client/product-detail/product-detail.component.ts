import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { faBars, faHeart, faInfo, faPhone, faRetweet, faShoppingBag, faStar, faStarHalf } from '@fortawesome/free-solid-svg-icons';
import { MessageService } from 'primeng/api';
import { CartService } from 'src/app/_service/cart.service';
import { ProductService } from 'src/app/_service/product.service';
import { WishlistService } from 'src/app/_service/wishlist.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css'],
  providers: [MessageService]

})
export class ProductDetailComponent implements OnInit {
  heart = faHeart;
  bag = faShoppingBag;
  phone = faPhone;
  bars = faBars;
  star = faStar;
  star_half = faStarHalf;
  retweet = faRetweet;
  info = faInfo;

  showDepartment = false;

  id: number = 0;
  product: any;
  listRelatedProduct: any[] = [];
  quantity: number = 1;

  constructor(private productService: ProductService, private router: Router, private route: ActivatedRoute, public cartService: CartService, public wishlistService: WishlistService, private messageService: MessageService) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;

  }
  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.getProduct();
  }

  showDepartmentClick() {
    this.showDepartment = !this.showDepartment;
  }


  getProduct() {
    this.productService.getProdct(this.id).subscribe({
      next: res => {
        this.product = res;
        this.getListRelatedProduct();
      }, error: err => {
        console.log(err);
      }
    })
  }



  getListRelatedProduct() {
    this.productService.getListRelatedProduct(this.product.category.id).subscribe({
      next: res => {
        this.listRelatedProduct = res;
      }, error: err => {
        console.log(err);
      }
    })
  }

  addToCart(item: any) {
    this.cartService.getItems();
    this.cartService.addToCart(item, 1);
    this.showSuccess("Đã thêm vào giỏ hàng!")

  }

  addCart(item: any) {
    this.cartService.getItems();
    this.cartService.addToCart(item, this.quantity);
    this.showSuccess("Đã thêm vào giỏ hàng!")
  }

  addToWishList(item: any) {
    if (!this.wishlistService.productInWishList(item)) {
      this.wishlistService.addToWishList(item);
      this.showSuccess("Đã thêm vào danh sách yêu thích!")
    }
  }

  plusQuantity() {
    this.quantity += 1;
  }
  subtractQuantity() {
    if (this.quantity > 1) {
      this.quantity -= 1;
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
