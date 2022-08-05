package com.sundayli.fastcopywin.repository;

import com.sundayli.fastcopywin.model.RecordData;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordDataRepository extends CrudRepository<RecordData, Integer> {

  @Query("select * from " + RecordData.TABLE_NAME + " order by id desc limit :n")
  List<RecordData> getRecordDataTopN(@Param("n") int n);

  @Query("delete from " + RecordData.TABLE_NAME + " where id in (select min(id) from " + RecordData.TABLE_NAME + ")")
  @Modifying
  boolean deleteLastRecord();
}
