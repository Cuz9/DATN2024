import { Component, OnInit } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { CategoryService } from 'src/app/_service/category.service';
import { ImageService } from 'src/app/_service/image.service';
import { ProductService } from 'src/app/_service/product.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
  providers: [MessageService, ConfirmationService],
})
export class ProductComponent implements OnInit {

  listProduct: any;
  listCategory: any;
  listImage: any;

  disabled: boolean = true;

  selectedFiles?: FileList;
  currentFile?: File;

  listImageChoosen: any = [];
  imageChoosen: any;

  selectedImageId: number | null = null;
  confirmDialog: boolean = false;
  displayConfirmation: boolean = false;


  onUpdate: boolean = false;
  showForm: boolean = false;
  showImage: boolean = false;
  showDelete: boolean = false;

  productForm: any = {
    name: null,
    description: null,
    price: null,
    quantity: null,
    categoryId: null,
    imageIds: []
  };

  constructor(private messageService: MessageService, private productService: ProductService, private imageService: ImageService, private categoryService: CategoryService, private confirmationService: ConfirmationService) {
  }


  ngOnInit(): void {
    this.getListProduct();
    this.getListCategoryEnabled();
    this.getListImage();
  }


  openNew() {
    this.onUpdate = false;
    this.showForm = true;
    this.listImageChoosen = [];
    this.productForm = {
      id: null,
      name: null,
      description: null,
      price: null,
      quantity: null,
      categoryId: null,
      imageIds: []
    }
  }

  openUpdate(data: any) {
    this.listImageChoosen = [];
    this.onUpdate = true;
    this.showForm = true;
    this.productForm.id = data.id;
    this.productForm.name = data.name;
    this.productForm.description = data.description;
    this.productForm.price = data.price;
    this.productForm.quantity = data.quantity;
    this.productForm.categoryId = data.category.id;
    data.images.forEach((res: any) => {
      this.listImageChoosen.push(res);
    })
  }


  onChooseImage() {
    this.showImage = true;
    this.disabled = true;
    let data = document.querySelectorAll('.list-image img');
    data.forEach(i => {
      i.classList.remove('choosen');
    })
  }


  getListProduct() {
    this.productService.getListProduct().subscribe({
      next: res => {
        this.listProduct = res;
        console.log(this.listProduct)
      }, error: err => {
        console.log(err);
      }
    })
  }

  getListCategoryEnabled() {
    this.categoryService.getListCategoryEnabled().subscribe({
      next: res => {
        this.listCategory = res;
      }, error: err => {
        console.log(err);
      }
    })
  }

  getListImage() {
    this.imageService.getList().subscribe({
      next: res => {
        this.listImage = res;
      }, error: err => {
        console.log(err);
      }
    })
  }

  uploadFile(event: any) {
    this.selectedFiles = event.target.files;
    if (this.selectedFiles) {
      const file: File | null = this.selectedFiles.item(0);
      if (file) {
        this.currentFile = file;
        this.imageService.upload(this.currentFile).subscribe({
          next: res => {
            this.currentFile = undefined;
            this.getListImage();
          }, error: err => {
          }
        })
      }
      this.currentFile = undefined;
    }
  }



  createProduct() {
    let data = this.listImageChoosen;
    data.forEach((res: any) => {
      this.productForm.imageIds.push(res.id);
    })
    const { name, description, price, quantity, categoryId, imageIds } = this.productForm;
    console.log(this.productForm);
    this.productService.createProduct(name, description, price, quantity, categoryId, imageIds).subscribe({
      next: res => {
        this.getListProduct();
        this.showForm = false;
        this.showSuccess("Thêm mới thành công");

      }, error: err => {
        this.showError(err.message);
      }
    })
  }

  updateProduct() {
    let data = this.listImageChoosen;
    data.forEach((res: any) => {
      this.productForm.imageIds.push(res.id);
    })
    const { id, name, description, price, quantity, categoryId, imageIds } = this.productForm;
    console.log(this.productForm);
    this.productService.updateProduct(id, name, description, price, quantity, categoryId, imageIds).subscribe({
      next: res => {
        this.getListProduct();
        this.showForm = false;
        this.showSuccess("Cập nhật thành công");
      }, error: err => {
        this.showError(err.message);
      }
    })

  }

  onDelete(id: number, name: string) {
    this.productForm.id = null;
    this.showDelete = true;
    this.productForm.id = id;
    this.productForm.name = name;
  }

  deleteProduct() {
    this.productService.deleteProduct(this.productForm.id).subscribe({
      next: res => {
        this.getListProduct();
        this.showWarn("Xóa thành công");
        this.showDelete = false;
      }, error: err => {
        this.showError(err.message);
      }
    })
  }

  chooseImage() {
    // Thêm hình ảnh đã chọn vào danh sách hình ảnh đã chọn
    this.listImageChoosen.push(this.imageChoosen);

    // Hiển thị dialog xác nhận trước khi cập nhật hình ảnh
    this.confirmationService.confirm({
      message: 'Bạn có chắc chắn muốn cập nhật hình ảnh?',
      header: 'Xác nhận',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        // Gửi danh sách hình ảnh đã chọn mới lên server để cập nhật thông tin sản phẩm
        const imageIds = this.listImageChoosen.map((image: any) => image.id);
        const { id, name, description, price, quantity, categoryId } = this.productForm;
        this.productService.updateProductImage(id, imageIds).subscribe({
          next: res => {
            // Nếu cập nhật thành công, cập nhật lại danh sách sản phẩm
            this.getListProduct();
            this.showForm = false;
            this.showSuccess("Cập nhật hình ảnh thành công");
          },
          error: err => {
            // Nếu có lỗi, hiển thị thông báo lỗi
            this.showError(err.message);
          }
        });

        // Sau khi cập nhật, ẩn cửa sổ chọn hình ảnh
        this.showImage = false;
      },
      reject: () => {
        // Nếu người dùng hủy bỏ xác nhận, không thực hiện hành động nào
      }
    });
  }


  // Hàm này được gọi khi người dùng xác nhận cập nhật hình ảnh
  confirmUpdate() {
    // Gọi hàm cập nhật hình ảnh tại đây
    this.updateImage(); // Đây là một ví dụ, bạn cần thay đổi nội dung hàm này để phù hợp với logic của bạn

    // Sau khi cập nhật, đóng dialog xác nhận
    this.confirmDialog = false;
  }


  selectImage(event: any, image: any) {
    let data = document.querySelectorAll('.list-image img');
    data.forEach(i => {
      i.classList.remove('choosen');
    })
    event.target.classList.toggle("choosen");
    this.imageChoosen = image;
    this.disabled = false;

    // Đánh dấu hình ảnh được chọn
    image.selected = !image.selected;
  }

  // Hàm kiểm tra xem hình ảnh có được chọn hay không
  isImageSelected(image: any): boolean {
    return image.selected;
  }


  showSuccess(text: string) {
    this.messageService.add({ severity: 'success', summary: 'Success', detail: text });
  }
  showError(text: string) {
    this.messageService.add({ severity: 'error', summary: 'Error', detail: text });
  }

  showWarn(text: string) {
    this.messageService.add({ severity: 'warn', summary: 'Warn', detail: text });
  }

  deleteImage(image: any) {
    const productId = this.productForm.id;
    const imageId = image.id;

    // Gọi phương thức deleteProductImage từ ProductService để xóa hình ảnh
    this.productService.deleteProductImage(productId, imageId).subscribe({
      next: res => {
        // Nếu xóa thành công, cập nhật lại danh sách ảnh
        this.listImageChoosen = this.listImageChoosen.filter((item: any) => item.id !== imageId);
        this.showSuccess("Hình ảnh sản phẩm đã được xóa!");
      },
      error: err => {
        // Nếu có lỗi, hiển thị thông báo lỗi
        // this.showError(err.message);
      }
    });
  }

  updateImage() {
    // Lấy danh sách ID của các hình ảnh đã chọn
    const imageIds = this.listImageChoosen.map((image: any) => image.id);

    // Lấy thông tin sản phẩm cần cập nhật
    const { id, name, description, price, quantity, categoryId } = this.productForm;

    // Gọi phương thức cập nhật hình ảnh từ ProductService
    this.productService.updateProductImage(id, imageIds).subscribe({
      next: res => {
        // Nếu cập nhật thành công, cập nhật lại danh sách sản phẩm
        this.getListProduct();
        this.showForm = false;
        this.showSuccess("Cập nhật hình ảnh thành công");
      },
      error: err => {
        // Nếu có lỗi, hiển thị thông báo lỗi
        this.showError(err.message);
      }
    });
  }

  cancelSelection() {
    // Xóa lựa chọn hình ảnh và ẩn cửa sổ chọn ảnh
    this.imageChoosen = null;
    this.showImage = false;
  }

  confirmSelection() {
    // Thêm hình ảnh đã chọn vào danh sách hình ảnh đã chọn
    this.listImageChoosen.push(this.imageChoosen);

    // Hiển thị dialog xác nhận trước khi cập nhật hình ảnh
    this.confirmDialog = true;
  }


}
