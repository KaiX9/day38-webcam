import { Component, OnInit, inject } from '@angular/core';
import { PhotoService } from '../photo.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {

  photoSvc = inject(PhotoService)
  photo = ""
  router = inject(Router)
  form!: FormGroup
  fb = inject(FormBuilder)
  file!: File

  ngOnInit(): void {
    if (!this.photoSvc.photo) {
      this.router.navigate([ '/' ]);
      return;
    }
    this.photo = this.photoSvc.photo;
    this.form = this.createForm();
    this.convertToFile(this.photo, 'photo').then(
      file => {
        console.info(file);
        this.file = file;
      }
    );
  }

  createForm(): FormGroup {
    return this.fb.group({
      comments: this.fb.control<string>('')
    });
  }
  
  upload() {
    const data = this.form.value;
    console.info('>>> comments: ', data.comments);
    
    firstValueFrom(this.photoSvc.upload(data['comments'], this.file))
      .then(result => {
        alert('uploaded')
        this.router.navigate([ '/' ])
      })
      .catch(err => {
        alert(JSON.stringify(err))
      })

  }

  // @ts-ignore, can use this to ignore error
  convertToFile(dataUrl: string, filename: string) {
    if (dataUrl.startsWith('data:')) {
      var arr = dataUrl.split(',');
      var matchResult = arr[0].match(/:(.*?);/);
      var u8arr = new Uint8Array();
      var n = 0;
      var mime = '';
      var bstr = '';
      if (matchResult) {
        var mime =  matchResult[1];
        var bstr = atob(arr[arr.length - 1]);
        var n = bstr.length;
        u8arr = new Uint8Array(n);
      }
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      var file = new File([u8arr], filename, {type:mime});
      return Promise.resolve(file);
    }
    return fetch(dataUrl)
        .then(res => res.arrayBuffer())
        .then(buf => new File([buf], filename, {type:mime}));
  }

}
