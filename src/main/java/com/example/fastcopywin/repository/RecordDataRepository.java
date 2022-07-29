package com.example.fastcopywin.repository;

import com.example.fastcopywin.model.RecordData;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordDataRepository extends CrudRepository<RecordData, Integer> {

  @Query("select * from RECORD_DATA order by id desc limit :n")
  List<RecordData> getRecordDataTopN(@Param("n") int n);
}
