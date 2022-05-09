package bvvs.chatserver.controllers;

import bvvs.chatserver.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(produces = "application/hal+json")
public class FileUploadController {
    @Autowired private FileUploadService fileUploadService;

    @PostMapping("/uploadFile")
    @ResponseStatus(CREATED)
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        return fileUploadService.uploadFile(file);
    }

    @GetMapping(value = "/getFile/", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getFile(@RequestParam String path) throws IOException {
        File file = new File(path);
        return Files.readAllBytes(file.toPath());
    }
}
