import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-maraicher',
  standalone: true,
  imports: [],
  templateUrl: './maraicher.component.html',
  styleUrl: './maraicher.component.css'
})
export class MaraicherComponent implements OnInit{

  constructor(private authService : AuthService) {
  }
  ngOnInit(): void {
    console.log(this.authService.userRank);
  }
}
