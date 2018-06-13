package com.example.Uploader.controller;

import com.example.Uploader.dto.ImagesDTO;
import com.example.Uploader.model.Image;
import com.example.Uploader.repository.ImageRepository;
import com.example.Uploader.utils.StringUtils;
import com.google.gson.Gson;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ImageController {

    private static final Gson gson = new Gson();
    private static final Logger log = Logger.getLogger(ImageController.class.getName());

    @Autowired
    private ImageRepository imageRepository;

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


    @GetMapping(value="/checkduplication/{md5Hash}")
    public ResponseEntity<String> checkImageDuplicationByMd5(@PathVariable String md5Hash){
        List<Image> images = imageRepository.findByMd5Hash(md5Hash);

        if(images.size() > 0){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

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


}
