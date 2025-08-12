import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard'; // ✅
import { CreateTest } from './components/create-test/create-test';


const routes: Routes = [
  { path: 'dashboard', component: Dashboard},
  { path: 'create-test', component: CreateTest}


];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
