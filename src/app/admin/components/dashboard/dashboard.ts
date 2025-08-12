import { Component } from '@angular/core';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Admin } from '../../Services/admin';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard {

  tests=[];


  constructor(
    private notification: NzNotificationService,
    private testService: Admin
  ) {}

  ngOnInit() {
    this.getAllTests();
  }
  
  getAllTests() {
    this.testService.getAllTest().subscribe(res => {
      this.tests = res;
    }, error => {
      this.notification
        .error(
          'ERROR',
          `Something Went Wrong. Try Again`,
          { nzDuration: 5000 }
        );
    });
  }
  
  
  getFormattedTime(time): string {
    const minutes = Math.floor(time / 60);
    const seconds = time % 60;
    return `${minutes} minutes ${seconds} seconds`;
  }
  

}
