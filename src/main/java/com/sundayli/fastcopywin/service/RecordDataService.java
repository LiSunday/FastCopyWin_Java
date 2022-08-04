package com.sundayli.fastcopywin.service;

import com.sundayli.fastcopywin.model.RecordData;
import com.sundayli.fastcopywin.repository.RecordDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordDataService {

  @Autowired
  RecordDataRepository recordDataRepository;

  public List<RecordData> getRecordDataTopN(int n) {
    return recordDataRepository.getRecordDataTopN(n);
  }

  public void saveRecord(RecordData recordData) {
    if (recordDataRepository.count() >= 500) {
      recordDataRepository.deleteLastRecord();
    }
    recordDataRepository.save(recordData);
  }
}
