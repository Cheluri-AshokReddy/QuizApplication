import { Component } from '@angular/core';
import { SharedModule } from '../../../modules/shared/shared-module';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Admin } from '../../Services/admin';

@Component({
  selector: 'app-create-test',
  imports: [SharedModule],
  templateUrl: './create-test.html',
  styleUrl: './create-test.scss'
})
export class CreateTest {

  constructor(private fb: FormBuilder,
  private devicesService: Admin,
  private notification: NzNotificationService,
  private router: Router,

  ){}

  testForm!: FormGroup;

ngOnInit() {
  this.testForm = this.fb.group({
    title: [null, Validators.required],
    description: [null, [Validators.required]],
    time: [null, [Validators.required]],
  });
}

submitForm(){
  if(this.testForm.valid){
    this.devicesService.createTest(this.testForm.value).subscribe(res=>{
      this.notification
      .success(
        'SUCCESS',
        `Test Created Successfully.`,
        { nzDuration: 5000 }
      );
      this.router.navigateByUrl("/admin/dashboard");
    }, error=>{
      this.notification
      .error(
        'ERROR',
        `${error.error}`,
        { nzDuration: 5000 }
      );
    });
  }
}



}
