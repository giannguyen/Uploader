import { Component, OnInit } from '@angular/core';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Component({
  selector: 'app-upload-resume',
  templateUrl: './upload-resume.component.html',
  styleUrls: ['./upload-resume.component.css']
})
export class UploadResumeComponent implements OnInit {

  filesToUpload: File;

  private serverUrl = 'http://localhost:8080/socket';
  private title = 'WebSockets chat';
  private stompClient;
  private ws:WebSocket;

  private message: String;

  constructor() { }

  ngOnInit() {
    this.initializeWebSocketConnection();
  }

  fileChangeEvent(fileInput: any) {
    this.filesToUpload = <File>fileInput.target.files[0];
    console.log(this.filesToUpload);
  }

  initializeWebSocketConnection() {


    setInterval( () => {

      if (this.ws && this.ws.readyState === 1) {
        return;
      }


      this.ws = new SockJS(this.serverUrl);
      this.stompClient = Stomp.over(this.ws);

      this.stompClient.connect({}, frame => { });

    }, 2000);


  }

  onSubmit() {
    // console.log(this.filesToUpload);

    let reader = new FileReader();

    reader.readAsDataURL(this.filesToUpload);

    reader.onload = () => {

      var bytebuffer = reader.result;

      console.log(bytebuffer);

      try{
        this.stompClient.send("/app/send/file", {}, bytebuffer);
        this.message = "Send file successfully";
      }
      catch(error){
        this.message = "Error occurred when send file";
      }

      alert(this.message);
    }

  }




}
