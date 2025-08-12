import { Component } from '@angular/core';
import { SharedModule } from '../../modules/shared/shared-module';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NzMessageService } from 'ng-zorro-antd/message';
import { Route, Router } from '@angular/router';
import { AuthService } from '../services/auth';

@Component({

  imports: [SharedModule],

  selector: 'app-signup',
  templateUrl: './signup.html',
  styleUrls: ['./signup.scss']
})
export class SignupComponent {

  constructor(
    private fb: FormBuilder,
    private message: NzMessageService,
    private router: Router,
    private authService: AuthService
  ) {}
  
  validateForm!: FormGroup;
  
  ngOnInit() {
    this.validateForm = this.fb.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]],
      name: [null, [Validators.required]]
    });
  }
  
  submitForm() {
    this.authService.register(this.validateForm.value).subscribe(
      res => {
        this.message.success(
          'Signup successful',
          { nzDuration: 5000 }
        );
        this.router.navigateByUrl('/login');
      },
      error => {
        this.message.error(
          `${error.error.error}`,
          { nzDuration: 5000 }
        );
      }
    );
  }
  

  
}
