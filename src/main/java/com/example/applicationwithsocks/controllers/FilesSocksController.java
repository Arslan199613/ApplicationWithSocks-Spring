package com.example.applicationwithsocks.controllers;
import com.example.applicationwithsocks.services.FileSocksService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

@RestController
@RequestMapping("/filesSocks")
public class FilesSocksController {
    private FileSocksService fileSocksService;

    public FilesSocksController(FileSocksService fileSocksService) {
        this.fileSocksService = fileSocksService;
    }
    @GetMapping("/import")

    public ResponseEntity<InputStreamResource> downloadDataFile() throws FileNotFoundException {
        File file = fileSocksService.getDataFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SocksLog.json\"")
                    .body(resource);

        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/export", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadDataFile(@RequestParam MultipartFile file) {
        fileSocksService.cleanDataFile();
        File dataFile = fileSocksService.getDataFile();
        try (FileOutputStream fos = new FileOutputStream(dataFile)) {

            IOUtils.copy(file.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
}