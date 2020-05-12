package org.example.flowkit.service;

import org.example.flowkit.entity.Associates;
import org.example.flowkit.entity.Document;
import org.example.flowkit.repository.DocumentRepository;
import org.example.flowkit.service.implementation.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class DocumentService implements DocumentServiceImpl {

    private DocumentRepository documentRepository;

    @Value("${document_upload_location}")
    private String document_location;

    public DocumentService() {
    }

    @Autowired
    public void setDocumentRepository(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document addDocument(String title, String path, Associates uploader) {
        Document document = new Document();
        document.setTitle(title);
        document.setPath(path);
        document.setUploader(uploader);
        try {
            documentRepository.save(document);
            return document;

        } catch (DataAccessException error) {
            System.out.println("Error: [addDocumentToActivityInstance][DocumentService] " +
                    error.getLocalizedMessage());
        }
        return null;
    }

    public String storeFile(MultipartFile file, Document document) {
        if (file.getOriginalFilename() == null) {
            return null;
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty()) {
            return null;
        }
        if (fileName.contains("..")) {
            fileName = fileName.replace("..", "_");
        }
        try (InputStream inputStream = file.getInputStream()) {
            Path upload_location = Paths.get(document_location);
            fileName = fileName + "_" + document.getId();
            Files.copy(inputStream, upload_location.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException error) {
            System.out.println("Error: [storeFile][DocumentService] " + error.getLocalizedMessage());
        }
        return null;
    }

    public Document updateDocumentTitle(String title, Document document) {
        document.setTitle(title);
        try {
            documentRepository.save(document);
            return document;

        } catch (DataAccessException error) {
            System.out.println("Error: [updateDocumentTitle][DocumentService] " +
                    error.getLocalizedMessage());
        }
        return null;
    }

    public Resource loadDocument(Document document) {
        try {
            Path upload_location = Paths.get(document.getPath());
            Path file = upload_location.resolve(document.getTitle());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException error) {
            System.out.println("Error: [loadDocument][DocumentService] " + error.getLocalizedMessage());
        }
        return null;
    }

    public Document getDocumentById(Long document_id) {
        Optional<Document> document = documentRepository.findById(document_id);
        return document.orElse(null);


    }

}
