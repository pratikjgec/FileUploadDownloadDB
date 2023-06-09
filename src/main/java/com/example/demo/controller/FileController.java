package com.example.demo.controller;

import java.io.IOException;
import java.util.Optional;

//import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.Entiry.FileEntity;
import com.example.demo.repository.FileRepository;

@RestController
public class FileController {

	@GetMapping("/")
	public String heath()
	{
		return "application working";
	}
	
	@Autowired
    private FileRepository fileRepository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setFileContent(file.getBytes());
            fileRepository.save(fileEntity);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }
	
	@GetMapping("/download/{id}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long id) {
	    Optional<FileEntity> fileOptional = fileRepository.findById(id);

	    if (fileOptional.isPresent()) {
	        FileEntity fileEntity = fileOptional.get();
	        ByteArrayResource resource = new ByteArrayResource(fileEntity.getFileContent());
	        String[] fileNameParts = fileEntity.getFileName().split("\\.");
	        String fileExtension = fileNameParts[fileNameParts.length - 1];
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName()+"."+fileExtension+"\"")
	                .body(resource);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
}
