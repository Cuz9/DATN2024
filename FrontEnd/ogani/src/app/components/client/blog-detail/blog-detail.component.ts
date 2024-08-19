import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BlogService } from 'src/app/_service/blog.service';
import { TagService } from 'src/app/_service/tag.service';

@Component({
  selector: 'app-blog-detail',
  templateUrl: './blog-detail.component.html',
  styleUrls: ['./blog-detail.component.css']
})
export class BlogDetailComponent implements OnInit {

  listTag: any;
  listBlogNewest: any;
  blog: any;
  id: any;
  formattedDate: string = '';

  constructor(private router: Router, private route: ActivatedRoute, private blogService: BlogService, private tagService: TagService) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    // Tính toán và định dạng ngày
    const currentDate = new Date();
    const day = currentDate.getDate().toString().padStart(2, '0');
    const month = (currentDate.getMonth() + 1).toString().padStart(2, '0');
    const year = currentDate.getFullYear();
    this.formattedDate = `${day}/${month}/${year}`;
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.getBlog();
    this.getListTag();
    this.getListNewest();
  }

  getListTag() {
    this.tagService.getListTag().subscribe({
      next: res => {
        this.listTag = res;
      }, error: err => {
        console.log(err);
      }
    })
  }

  getBlog() {
    this.blogService.getBlog(this.id).subscribe({
      next: res => {
        this.blog = res;
        console.log(this.blog);
      }, error: err => {
        console.log(err);
      }
    })
  }

  getListNewest() {
    this.blogService.getListNewest(3).subscribe({
      next: res => {
        this.listBlogNewest = res;
      }, error: err => {
        console.log(err);
      }
    })
  }


}
