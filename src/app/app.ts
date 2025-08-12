import { Component, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { SharedModule } from './modules/shared/shared-module';
import { UserStorageService } from './auth/services/user-storage';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, SharedModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('quizWeb');

  isUserLoggedIn: boolean = UserStorageService.isUserLoggedIn();
isAdminLoggedIn: boolean = UserStorageService.isAdminLoggedIn();

constructor(private router: Router) {}

ngOnInit() {
  this.router.events.subscribe(event => {
    this.isUserLoggedIn = UserStorageService.isUserLoggedIn();
    this.isAdminLoggedIn = UserStorageService.isAdminLoggedIn();
  });
}

logout() {
  UserStorageService.signOut();
  this.router.navigateByUrl('login');
}

}
