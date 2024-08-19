import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AuthService } from 'src/app/_service/auth.service';
import { StorageService } from 'src/app/_service/storage.service';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css'],
  providers: [MessageService]

})
export class LoginPageComponent implements OnInit {


  isSuccessful = false;
  isSignUpFailed = false;
  isLoggedIn = false;
  isLoginFailed = false;
  roles: string[] = [];
  errorMessage = '';

  loginForm: any = {
    username: null,
    password: null
  }

  registerForm: any = {
    username: null,
    email: null,
    password: null
  }

  constructor(private authService: AuthService, private storageService: StorageService, private messageService: MessageService, private router: Router) { }

  ngOnInit(): void {
  }

  login(): void {
    const { username, password } = this.loginForm;
    console.log(this.loginForm);
    this.authService.login(username, password).subscribe({
      next: res => {
        this.storageService.saveUser(res);
        this.isLoggedIn = true;
        this.isLoginFailed = false;
        this.roles = this.storageService.getUser().roles;

        // Kiểm tra xem mảng roles có chứa role "ROLE_ADMIN" không
        if (this.roles.includes("ROLE_ADMIN")) {
          // Thực hiện hành động nào đó khi roles chứa role "ROLE_ADMIN"
          console.log("User là admin, thực hiện hành động cho admin");
          this.router.navigate(['/admin']);
        } else {
          // Thực hiện hành động khác khi roles không chứa role "ROLE_ADMIN"
          console.log("User không phải là admin, thực hiện hành động cho người dùng khác");
          this.router.navigate(['/']);
        }

        this.showSuccess("Đăng nhập thành công!!");
        
      }, error: err => {
        console.log(err);
        this.isLoggedIn = false;
        this.isLoginFailed = true;
      }
    })
  }

  register(): void {
    const { username, email, password } = this.registerForm;
    console.log(this.registerForm);
    this.authService.register(username, email, password).subscribe({
      next: res => {
        this.isSuccessful = true;
        this.isSignUpFailed = false;
        this.showSuccess("Đăng ký thành công")
        this.loginForm.username = username;
        this.loginForm.password = password;
        this.login();
      }, error: err => {
        this.showError(err.message);
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    })
  }

  loginFormChange() {
    document.getElementById('container')?.classList.remove("right-panel-active");
  }
  registerFormChange() {
    document.getElementById('container')?.classList.add("right-panel-active");
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
