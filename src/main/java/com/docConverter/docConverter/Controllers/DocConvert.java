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

            return "Hello, World!";
        }catch (Exception e){
            e.printStackTrace();
            return "Hello, World!";
        }

    }


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






