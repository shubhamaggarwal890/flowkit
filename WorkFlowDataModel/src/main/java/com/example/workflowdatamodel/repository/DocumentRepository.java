package com.example.workflowdatamodel.repository;

import com.example.workflowdatamodel.entity.Document;
import org.springframework.data.repository.CrudRepository;

public interface DocumentRepository extends CrudRepository<Document, Long> {
}
