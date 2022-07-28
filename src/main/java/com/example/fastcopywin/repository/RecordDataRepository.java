package com.example.fastcopywin.repository;

import com.example.fastcopywin.model.RecordData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RecordDataRepository extends CrudRepository<RecordData, Integer> {
}
