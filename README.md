Excersite have two part: Backend using Spring Boot(Java), Front End using Angular.

## Backend (contained in Uploader folder)

### Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.example.Uploader.UploaderApplication` class from your IDE (Intellij IDEA, Eclipse).

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```
## Frontend (contained in image-angular folder)

### Install NodeJs and Angular on local

1. Get and install NodeJs from [here](https://nodejs.org/en/)
2. Install Angular by run `npm i -g @angular/cli`

### Install npm packages

Install the npm packages described in the `package.json` and verify that it works:

```shell
npm install
npm start
```

## Function
### 1. Basic functionality
-   `POST /upload`: Upload an image using HTTP POST.
```java
@PostMapping(value = "/upload")  
public ResponseEntity upload(HttpServletResponse response, HttpServletRequest request){  
  
    log.info("start upload image");  
  
    try {  
  
        log.info(request.getParameter("md5Hash"));  
  
        String md5Hash = request.getParameter("md5Hash");  
  
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
        Iterator<String> it = multipartRequest.getFileNames();  
        MultipartFile multipartFile = multipartRequest.getFile(it.next());  
        String fileName = StringUtils.randomImageName() +".png";  
  
        byte[] bytes = multipartFile.getBytes();  
        BufferedOutputStream stream = new BufferedOutputStream(  
                new FileOutputStream(new File("src/main/resources/static/image/"+fileName)));  
  
        stream.write(bytes);  
        stream.close();  
  
        Image image = new Image();  
  
        image.setName(fileName);  
        image.setmd5Hash(md5Hash);  
  
  
        imageRepository.save(image);  
  
        return new ResponseEntity("Upload Success!", HttpStatus.OK);  
    } catch (Exception e) {  
        e.printStackTrace();  
        return new ResponseEntity("Upload failed!", HttpStatus.BAD_REQUEST);  
    }  
  
}
```
-   `GET /images`: Return list of all image urls as format
```java
@GetMapping(value="/images", produces = {MediaType.APPLICATION_JSON_VALUE})  
public ResponseEntity getAllImages(){  
    List<Image> images = imageRepository.findAll();  
  
    List<String> urls = new ArrayList<>();  
  
    images.stream()  
            .forEach(image -> urls.add ("http://localhost:8080/images/" + image.getId()));  
  
    ImagesDTO imagesDTO = new ImagesDTO();  
    imagesDTO.setData(urls);  
  
    Gson gson = new Gson();  
  
    return new ResponseEntity(gson.toJson(imagesDTO), HttpStatus.OK);  
}
```
-   `GET /image/<id>`: Download the image.
```java
@GetMapping(value = "/images/{id}")  
public @ResponseBody  
HttpEntity<byte[]> downloadImage(@PathVariable Long id) throws IOException {  
  
    Image image = imageRepository.findOne(id);  
  
    if (image == null){  
        return new ResponseEntity(HttpStatus.NOT_FOUND);  
    }  
  
     InputStream inputStream = new FileInputStream(  
             new File("src/main/resources/static/image/"+image.getName()));  
  
    Path path = Paths.get("src/main/resources/static/image/"+image.getName());  
      
    byte[] imageByte = Files.readAllBytes(path);  
    HttpHeaders headers = new HttpHeaders();  
    headers.setContentType(MediaType.IMAGE_JPEG);  
    headers.setContentLength(imageByte.length);  
  
    return new HttpEntity<>(imageByte, headers);  
  
}
```
### 2. Check duplicate images
1. Using md5 to hash file to string
```typescript
hashFile(file:  File) {
	let  hasher  =  new  ParallelHasher('../assets/js/md5_worker.js');

	return  hasher.hash(file);
}
```
2. Send md5 to backend to check duplicate
```java
@GetMapping(value="/checkduplication/{md5Hash}")  
public ResponseEntity<String> checkImageDuplicationByMd5(@PathVariable String md5Hash){  
    List<Image> images = imageRepository.findByMd5Hash(md5Hash);  
  
    if(images.size() > 0){  
        return new ResponseEntity<>(HttpStatus.CONFLICT);  
    }  
    return new ResponseEntity<>(HttpStatus.OK);  
}
```
3. If md5Hash does exist, response to user. Otherwise send file and md5Hash to server.

### 3. Retry/Partial uploading (Nice to have)

####  - Backend
1. Configure Web Socket Message( `endpoint, maxsize, broker`)
```java
@Configuration  
@EnableWebSocketMessageBroker  
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {  
    @Override  
  public void registerStompEndpoints(StompEndpointRegistry registry) {  
        registry.addEndpoint("/socket")  
                .setAllowedOrigins("*")  
                .withSockJS();  
    }  
      
    @Override  
  public void configureWebSocketTransport(WebSocketTransportRegistration registration) {  
        registration.setMessageSizeLimit(10240000*1024);  
    }  
  
    @Override  
  public void configureMessageBroker(MessageBrokerRegistry registry) {  
        registry.setApplicationDestinationPrefixes("/app")  
                .enableSimpleBroker("/file");  
    }  
}
```
2. Set up controller to handle message from client
```java
@MessageMapping("/send/file")  
public void onRecievedFile(String message) throws IOException {  
  
    String[] byte64 = message.split(",");  
  
    byte[] data = Base64.decodeBase64(byte64[1]);  
  
    String fileName = StringUtils.randomImageName() + ".png";  
  
    BufferedOutputStream stream = new BufferedOutputStream(  
            new FileOutputStream("src/main/resources/static/image/" + fileName));  
      
    stream.write(data);  
  
    stream.close();  
}
``` 
#### - Frontend

1. Create stomjs
```javascript
initializeWebSocketConnection() {
	setInterval( () => {
		if (this.ws  &&  this.ws.readyState  ===  1) {
			return;
		}
		this.ws  =  new  SockJS(this.serverUrl);
		this.stompClient  =  Stomp.over(this.ws);
		this.stompClient.connect({}, frame  => { });
	}, 2000);
}
```
2. Send data to server
```javascript
this.stompClient.send("/app/send/file", {}, bytebuffer);
```


