package com.docConverter.docConverter.Controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import com.syncfusion.ej2.wordprocessor.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.web.bind.annotation.CrossOrigin;
import com.docConverter.docConverter.helper.CdnToFile;

import static com.docConverter.docConverter.helper.CdnToFile.*;


@RestController
@RequestMapping("/api")
public class DocConvert {


    @GetMapping("/hello")
    public String sayHello() {
        try {
            MultipartFile file = convertFromCDN("https://sample117sample1.s3.ap-south-1.amazonaws.com/sample117sample1/ed9275cc-6f2c-4d40-8371-f0c29d2f514a?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAXUQCJIP5XFFDPSPT%2F20231204%2Fap-south-1%2Fs3%2Faws4_request&X-Amz-Date=20231204T085420Z&X-Amz-Expires=600&X-Amz-Signature=988fc45018b292efd61b13c828cb0a90d0514b8fbbdb88ecbceecf93c6914213&X-Amz-SignedHeaders=host");
            System.out.println(file);
            return "Hello, World!";
        }catch (Exception e){
            e.printStackTrace();
            return "Hello, World!";
        }

    }

//    @CrossOrigin(origins = "http://localhost:4200") // Replace with your frontend origin
//    @GetMapping("/readFile")
//    public String readFile() throws IOException {
//        ClassPathResource resource = new ClassPathResource("sample.json");
//        InputStream inputStream = resource.getInputStream();
//
//        // Read the content of the file into a String
//        byte[] fileBytes = FileCopyUtils.copyToByteArray(inputStream);
//        String fileContent = new String(fileBytes, StandardCharsets.UTF_8);
//
//        return fileContent;
//    }

    @CrossOrigin(origins = "http://localhost:3000") // Replace with your frontend origin
    @PostMapping("/readFile")
    public String readFile(@RequestParam("url") String url) throws IOException {

        try {
            Path path = getDocumentStream(url);
            System.out.println(path);
            InputStream inputStream = Files.newInputStream(path);
            return WordProcessorHelper.load(inputStream, FormatType.Docx);
        }catch(Exception e){
            e.printStackTrace();
//            if(e.getMessage()=="not Found"){
//                return "file session expired";
//            }
           return  "{\"sections\":[{\"blocks\":[{\"inlines\":[{\"text\":" + e.getMessage() + "}]}]}]}";
        }

    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/convert")
    public String uploadFile(@RequestParam("files") MultipartFile file) throws Exception {
        try {
            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            String fileExtension = StringUtils.getFilenameExtension(fileName);
            System.out.println(fileName+ "===="+fileSize+"====="+fileExtension);
            if(!fileExtension.equals("docx")){
                return "incorrect File Extention";
            }
            return WordProcessorHelper.load(file.getInputStream(), FormatType.Docx);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"sections\":[{\"blocks\":[{\"inlines\":[{\"text\":" + e.getMessage() + "}]}]}]}";
        }
    }



}






