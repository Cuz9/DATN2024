import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { faHeart, faInfo, faRetweet, faShoppingBag } from '@fortawesome/free-solid-svg-icons';
import { MessageService } from 'primeng/api';
import { CartService } from 'src/app/_service/cart.service';
import { CategoryService } from 'src/app/_service/category.service';
import { ProductService } from 'src/app/_service/product.service';
import { WishlistService } from 'src/app/_service/wishlist.service';


@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.css'],
  providers: [MessageService]

})
export class ShopComponent implements OnInit {

  heart = faHeart;
  bag = faShoppingBag;
  retweet = faRetweet;
  info = faInfo;

  id: number = 0;
  listProduct : any;
  listCategory : any;
  listProductNewest : any[] = [];

  rangeValues = [0,500000];

  constructor(
    private categoryService:CategoryService,
    private productService: ProductService,
    private router: Router,
    private route: ActivatedRoute,
    public cartService:CartService,
    public wishlistService:WishlistService,
    private messageService: MessageService
  ){
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;

  }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    // this.getListProductByCategory();
    this.getListProduct();
    this.getListCategoryEnabled();
    this.getNewestProduct();
  }

  getListProduct(){
    this.productService.getListProduct().subscribe({
      next: res =>{
        this.listProduct = res;
        console.log(this.listProduct);
      },error: err =>{
        console.log(err);
      } 
    })
  }

  getListProductByCategory(categoryId: number){
    this.productService.getListByCategory(categoryId).subscribe({
      next: res =>{
        this.listProduct = res;
      },error: err =>{
        console.log(err);
      } 
    })
  }

  getListCategoryEnabled(){
    this.categoryService.getListCategoryEnabled().subscribe({
      next: res =>{
        this.listCategory = res;
      },error: err=>{
        console.log(err);
      }
    })
  }

  getNewestProduct(){
    this.productService.getListProductNewest(4).subscribe({
      next:res =>{
        this.listProductNewest = res;
      },error: err =>{
        console.log(err);
      }
    })
  }

  getListProductByPriceRange(){
    this.productService.getListByPriceRange(this.id,this.rangeValues[0],this.rangeValues[1]).subscribe({
      next: res =>{
        this.listProduct = res;
        console.log(this.listProduct);
      },error: err =>{
        console.log(err);
      }
    })
  }

  addToCart(item: any){
    this.cartService.getItems();
    this.cartService.addToCart(item,1);
    this.showSuccess("Đã thêm vào giỏ hàng!")
  }
  
  addToWishList(item: any){
    if(!this.wishlistService.productInWishList(item)){
      this.wishlistService.addToWishList(item);
      this.showSuccess("Đã vào danh saách yêu thích!")
    }
  }

  showSuccess(text: string) {
    this.messageService.add({severity:'success', summary: 'Success', detail: text});
  }

  showError(text: string) {
    this.messageService.add({severity:'error', summary: 'Error', detail: text});
  }
  
  showWarn(text: string) {
    this.messageService.add({severity:'warn', summary: 'Warn', detail: text});
  }

}
