import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-role-page',
  templateUrl: './role-page.component.html',
  styleUrl: './role-page.component.css'
})
export class RolePageComponent {
  constructor(private route: ActivatedRoute) {}

  get title(): string {
    return this.route.snapshot.data['title'];
  }

  get description(): string {
    return this.route.snapshot.data['description'];
  }
}
