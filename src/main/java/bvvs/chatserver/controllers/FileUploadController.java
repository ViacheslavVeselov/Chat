package bvvs.chatserver.controllers;

import bvvs.chatserver.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(value = "/uploadFile", produces = "application/hal+json")
public class FileUploadController {
    @Autowired private FileUploadService fileUploadService;

    @PostMapping()
    @ResponseStatus(CREATED)
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        return fileUploadService.uploadFile(file);
    }
}
