package org.example.flowkit.service;

import org.example.flowkit.entity.Associates;
import org.example.flowkit.entity.Document;
import org.example.flowkit.repository.ActivityInstanceRepository;
import org.example.flowkit.repository.DocumentRepository;
import org.example.flowkit.service.implementation.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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
