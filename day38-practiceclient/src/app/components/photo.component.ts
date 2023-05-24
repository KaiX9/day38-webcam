import { Component, inject } from '@angular/core';
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
  router = inject(Router)
  photoSvc = inject(PhotoService)

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

}
