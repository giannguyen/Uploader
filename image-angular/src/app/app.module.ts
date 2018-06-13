import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { FileUploadComponent } from './component/file-upload/file-upload.component';
import { UploadImageService } from './service/upload-image.service';
import { Md5hashService } from './service/md5hash.service';
import { HttpModule } from '@angular/http';
import { UploadResumeComponent } from './component/upload-resume/upload-resume.component';


@NgModule({
  declarations: [
    AppComponent,
    FileUploadComponent,
    UploadResumeComponent
  ],
  imports: [
    BrowserModule,
    HttpModule
  ],
  providers: [UploadImageService, Md5hashService],
  bootstrap: [AppComponent]
})
export class AppModule { }
