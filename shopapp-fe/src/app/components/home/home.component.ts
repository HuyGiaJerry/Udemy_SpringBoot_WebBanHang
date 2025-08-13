import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { environment } from 'src/app/environments/environment';
import { Product } from 'src/app/models/product';
import { ProductService } from 'src/app/services/product.service';
@Component({
  selector: 'app-home',

  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  products: Product[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 9;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.getAllProducts(this.currentPage, this.itemsPerPage);
  }


  getAllProducts(page: number, limit: number) {
    this.productService.getProducts(page, limit).subscribe(
      {
        next: (response: any) => {
          debugger
          response.products.forEach((product: Product) => {
            debugger
            product.url = `${environment.apiUrl}/products/images/${product.thumpnail}`;
          });
          this.products = response.products;
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
    this.getAllProducts(this.currentPage, this.itemsPerPage);
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
