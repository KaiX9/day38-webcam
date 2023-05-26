import { Component, ElementRef, ViewChild, inject } from '@angular/core';
import { Router } from '@angular/router';
import { WebcamImage } from 'ngx-webcam';
import { Subject } from 'rxjs';
import { PhotoService } from '../photo.service';

@Component({
  selector: 'app-photo',
  templateUrl: './photo.component.html',
  styleUrls: ['./photo.component.css']
})
export class PhotoComponent {

  showWebcam: boolean = true
  trigger: Subject<void> = new Subject<void>()
  webcamImage!: WebcamImage
  idArray: string[] = []
  router = inject(Router)
  photoSvc = inject(PhotoService)

  @ViewChild('uploadFile')
  uploadFile!: ElementRef

  snapshot() {
    this.trigger.next();
    this.router.navigate([ '/upload' ]);
  }

  toggleWebcam() {
    this.showWebcam = !this.showWebcam;
  }

  handleCapturedImage(webcamImage : WebcamImage) {
    console.info(">>> ", webcamImage);
    this.webcamImage = webcamImage;
    this.photoSvc.photo = webcamImage.imageAsDataUrl;
  }

  submitFile() {
    const f: File = this.uploadFile.nativeElement.files[0];
    this.photoSvc.file = f as any;
    console.info('>> file from folder: ', f)
    if (f === undefined) {
      this.router.navigate([ '/' ]);
    } else {
      this.photoSvc.photo = "";
      console.info('photo: ', this.photoSvc.photo)
      this.router.navigate([ '/upload' ]);
    }
  }

}
