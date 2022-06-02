package com.project.farmnode.controller;

import com.project.farmnode.model.Image;
import com.project.farmnode.repository.ImageRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {
    public static String uploadDirectory = System.getProperty("user.dir")+"/uploads";
    @Autowired
    ImageRepo imageRepository;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {

        /*Image img = new Image( file.getOriginalFilename(),file.getContentType(),file.getBytes() );


        final Image savedImage = imageRepository.save(img);


        System.out.println("Image saved");


        return savedImage;*/

        //StringBuilder filenames = new StringBuilder();


        String newFile;
        do{
            newFile=generateUniqueFileName() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        }
        while(new File(uploadDirectory+newFile).exists());

        Path filenameAndPath = Paths.get(uploadDirectory, newFile );
        try{
            Files.write(filenameAndPath,file.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return newFile;
    }

    @RequestMapping(value = "/produce-image/{filename}", method = RequestMethod.GET)
    public byte[] getProduceImage(@PathVariable(value = "filename") String filename , HttpServletResponse response) throws IOException {
        return Files.readAllBytes(Paths.get(uploadDirectory+"/produce/"+filename));
    }

    @RequestMapping(value = "/user-image/{filename}", method = RequestMethod.GET)
    public byte[] getUserImage(@PathVariable(value = "filename") String filename, HttpServletResponse response) throws IOException {
        return Files.readAllBytes(Paths.get(uploadDirectory+"/user/"+filename));
    }

    String generateUniqueFileName() {
        String filename = "";
        long millis = System.currentTimeMillis();
        String datetime = new Date().toGMTString();
        datetime = datetime.replace(" ", "");
        datetime = datetime.replace(":", "");
        String rndchars = RandomStringUtils.randomAlphanumeric(16);
        filename = rndchars + "_" + datetime + "_" + millis;
        return filename;
    }

    @RequestMapping(value = "/sid", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(HttpServletResponse response) throws IOException {

        var imgFile = new ClassPathResource("image/sid.jpg");

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }


}
