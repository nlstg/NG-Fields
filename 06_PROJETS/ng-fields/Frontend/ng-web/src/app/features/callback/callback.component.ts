import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { timer, race } from 'rxjs';
import { first, tap, mapTo } from 'rxjs/operators';

@Component({
  selector: 'app-callback',
  standalone: true,
  templateUrl: './callback.component.html',
  styleUrl: './callback.component.css',
})
export class CallbackComponent implements OnInit {
  private auth = inject(AuthService);
  private router = inject(Router);

  ngOnInit(): void {
    race(
      this.auth.isAuthenticated$.pipe(first(Boolean), tap(() => this.router.navigate(['/dashboard']))),
      timer(15000).pipe(tap(() => this.router.navigate(['/login']))),
    ).subscribe();
  }
}
