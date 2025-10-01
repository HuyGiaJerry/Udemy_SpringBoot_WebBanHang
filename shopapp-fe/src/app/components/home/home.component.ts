import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { environment } from 'src/app/environments/environment';
import { Product } from 'src/app/models/product';
import { Category } from 'src/app/models/category';
import { ProductService } from 'src/app/services/product.service';
import { CategoryService } from 'src/app/services/category.service';
@Component({
  selector: 'app-home',

  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  products: Product[] = [];
  currentPage: number = 0;
  categories: Category[] = [];
  itemsPerPage: number = 9;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = '';
  selectedCategoryId: number = 0;

  constructor(private productService: ProductService,private categoryService: CategoryService) { }

  ngOnInit(): void {
    this.getAllProducts(this.keyword,this.selectedCategoryId,this.currentPage, this.itemsPerPage);
    this.getCategories(1,100);
  }

  getCategories(page: number , limit: number) {
    this.categoryService.getCategories(page, limit).subscribe(
      {
        next: (categories: Category[]) => {
          debugger
          this.categories = categories;
        },
        complete: () => {
          debugger;
        },
        error: (error: any) => {
          console.error('Error fetching categories:', error);
        }
      });
  }

  searchProducts(){
    this.currentPage = 0;
    this.itemsPerPage = 9;
    debugger
    this.getAllProducts(this.keyword,this.selectedCategoryId,this.currentPage, this.itemsPerPage);
  }


  getAllProducts(keyword: string, category_id: number, page: number, limit: number) {
    this.productService.getProducts(keyword, category_id, page, limit).subscribe(
      {
        next: (response: any) => {
          debugger
          response.products.forEach((product: Product) => {
            
            product.url = `${environment.apiUrl}/products/images/${product.thumpnail}`;
          });
          this.products = response.products;
          console.log('products:', this.products);
          this.totalPages = response.totalPages;
          this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
        },
        complete: () => {
          debugger;
        },
        error: (error: any) => {
          debugger
          console.error('Error fetching products:', error);
        }
      });
  }

  




  onPageChange(page: number) {
    debugger
    this.currentPage = page;
    this.getAllProducts(this.keyword,this.selectedCategoryId,this.currentPage, this.itemsPerPage);
  }

  generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);

    let startPage = Math.max(0, currentPage - halfVisiblePages);
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages - 1);

    const pages: number[] = [];
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  }


}
