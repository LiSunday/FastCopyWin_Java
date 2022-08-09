package com.sundayli.fastcopywin.service;

import com.sundayli.fastcopywin.model.RecordData;
import com.sundayli.fastcopywin.repository.RecordDataRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RecordDataService {

  @Autowired
  RecordDataRepository recordDataRepository;

  public List<RecordData> getRecordDataTopN(int n) {
    return recordDataRepository.getRecordDataTopN(n);
  }

  @Async
  public void saveRecordAsync(RecordData recordData) {
    if (recordDataRepository.count() >= 500) {
      recordDataRepository.deleteLastRecord();
    }
    recordDataRepository.save(recordData);
  }
}
