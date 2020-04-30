package com.example.workflowdatamodel.service;

import com.example.workflowdatamodel.entity.ActivityInstance;
import com.example.workflowdatamodel.entity.Associates;
import com.example.workflowdatamodel.entity.Document;
import com.example.workflowdatamodel.repository.ActivityInstanceRepository;
import com.example.workflowdatamodel.repository.DocumentRepository;
import com.example.workflowdatamodel.service.implementation.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DocumentService implements DocumentServiceImpl {

    private DocumentRepository documentRepository;
    private ActivityInstanceRepository activityInstanceRepository;

    public DocumentService() {
    }

    @Autowired
    public void SetDocumentService(DocumentRepository documentRepository, ActivityInstanceRepository activityInstanceRepository) {
        this.documentRepository = documentRepository;
        this.activityInstanceRepository = activityInstanceRepository;
    }

    public Document addDocumentToActivityInstance(String title, String path, Associates uploader) {
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
}
