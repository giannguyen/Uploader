import { Component, OnInit } from '@angular/core';
import { UploadImageService } from '../../service/upload-image.service';
import { Md5hashService } from '../../service/md5hash.service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  filesToUpload: File;

  md5Hash: String;

  constructor(private uploadImageService: UploadImageService,
    private md5HashService: Md5hashService) { }

  ngOnInit() {
  }

  onSubmit() {

    this.md5HashService.hashFile(this.filesToUpload).then(res=>{
      // console.log(res);

      this.md5Hash = res;

      this.uploadImageService.checkImageDuplicationByMd5(res).subscribe(res=>{
        console.log(res);
        // let md5hash = JSON.parse(JSON.stringify(res))._body;

        this.uploadImageService.makeFileRequest("http://localhost:8080/upload",
            this.md5Hash, this.filesToUpload).then(res=>{
              alert("File uploaded successful.")
        });

      }, err =>{
        console.log(err);
        alert("Image file does exist. Please upload other files.");
      })
    });

    // this.uploadImageService.upload();
  }


  fileChangeEvent(fileInput: any) {
    this.filesToUpload = <File>fileInput.target.files[0];
    console.log(this.filesToUpload);
  }

}
