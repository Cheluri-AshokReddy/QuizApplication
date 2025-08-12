import { Routes } from '@angular/router';
import { SignupComponent } from './auth/signup/signup';
import { Login } from './auth/login/login';

export const routes: Routes = [
    { path: 'register', component: SignupComponent },
    { path: 'login', component: Login },
    {
      path: 'user',
      loadChildren: () =>
        import('./user/user-module').then(m => m.UserModule),
    },
    {
      path: 'admin',
      loadChildren: () =>
        import('./admin/admin-module').then(m => m.AdminModule),
    }
  ];
  