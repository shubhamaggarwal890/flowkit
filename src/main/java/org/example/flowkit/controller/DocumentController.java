package org.example.flowkit.controller;

import org.example.flowkit.entity.Associates;
import org.example.flowkit.entity.Document;
import org.example.flowkit.service.AssociateService;
import org.example.flowkit.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentController {

    private DocumentService documentService;
    @Value("${document_upload_location}")
    private String document_location;
    private AssociateService associateService;

    @Autowired
    public void setAssociateService(AssociateService associateService) {
        this.associateService = associateService;
    }

    @Autowired
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload_document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Document uploadDocument(@RequestParam MultipartFile file, @RequestParam Long uploader) {
        Associates upload = associateService.getAssociateById(uploader);
        if (upload == null) {
            return null;
        }
        Document document = documentService.addDocument(null, document_location, upload);
        String file_name = documentService.storeFile(file, document);
        if (file_name == null) {
            return null;
        }
        Document uploaded = documentService.updateDocumentTitle(file_name, document);
        if (uploaded == null) {
            return null;
        }
        uploaded.setUploader(null);
        uploaded.setTitle(null);
        uploaded.setPath(null);
        uploaded.setActivityInstance(null);
        return uploaded;
    }

    @PostMapping(value = "/get_document")
    public ResponseEntity<Resource> uploadDocument(@RequestBody Document document) {
        System.out.println(document.getId());
        Document uploaded = documentService.getDocumentById(document.getId());
        if(uploaded==null){
            return ResponseEntity.notFound().build();
        }
        Resource file = documentService.loadDocument(uploaded);
        if(file==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;name="+file.getFilename()).body(file);
    }
}
