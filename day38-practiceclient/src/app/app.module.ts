import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { WebcamModule } from 'ngx-webcam';
import { PhotoComponent } from './components/photo.component';
import { UploadComponent } from './components/upload.component';
import { Routes, RouterModule } from '@angular/router';
import { PhotoService } from './photo.service';

const routes: Routes = [
  { path: '', component: PhotoComponent },
  { path: 'upload', component: UploadComponent },
  { path: '**', redirectTo: '/', pathMatch: 'full' }
]

@NgModule({
  declarations: [
    AppComponent,
    PhotoComponent,
    UploadComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    WebcamModule,
    RouterModule.forRoot(routes)
  ],
  providers: [PhotoService],
  bootstrap: [AppComponent]
})
export class AppModule { }
