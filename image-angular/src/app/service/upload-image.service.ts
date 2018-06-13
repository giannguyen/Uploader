import { Injectable } from '@angular/core';
import {Http, Headers} from '@angular/http';
import { StringifyOptions } from 'querystring';
import { Md5hashService } from './md5hash.service';

@Injectable()
export class UploadImageService {

	

	constructor(private md5HashService: Md5hashService,
				private http:Http) { }


	upload(md5Hash: String, filesToUpload) {
		this.makeFileRequest("http://localhost:8080/upload", md5Hash ,filesToUpload).then((result) => {
			console.log(result);
		}, (error) => {
			console.log(error);
		});
	}

	

	makeFileRequest(url: string,md5Hash:String, file: File) {
		return new Promise((resolve, reject) => {
			var formData: any = new FormData();
			var xhr = new XMLHttpRequest();
			formData.append("uploads[]", file, file.name);

			formData.append("md5Hash", md5Hash);

			xhr.onreadystatechange = function () {
				if (xhr.readyState == 4) {
					if (xhr.status == 200) {
						console.log("image uploaded successfully!");
						resolve(xhr.response);
					} else {
						reject(xhr.response);
					}
				}
			}

			xhr.open("POST", url, true);

			xhr.send(formData);
		});
	}
	
	checkImageDuplicationByMd5(md5Hash: String){
		let headers = new Headers ({
			'Content-Type': 'application/json'
		  });
		let url = "http://localhost:8080/checkduplication/" + md5Hash;
		return this.http.get(url, {headers: headers});
	}

}
