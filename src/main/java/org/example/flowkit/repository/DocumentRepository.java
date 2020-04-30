package org.example.flowkit.repository;

import org.example.flowkit.entity.Document;
import org.springframework.data.repository.CrudRepository;

public interface DocumentRepository extends CrudRepository<Document, Long> {
}
