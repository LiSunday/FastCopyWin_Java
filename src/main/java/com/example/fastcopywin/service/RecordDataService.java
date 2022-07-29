package com.example.fastcopywin.service;

import com.example.fastcopywin.model.RecordData;
import com.example.fastcopywin.repository.RecordDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordDataService {

  @Autowired
  RecordDataRepository recordDataRepository;

  public List<String> getRecordDataTopN(int n) {
    return recordDataRepository.getRecordDataTopN(n).stream().map(RecordData::getData).collect(Collectors.toList());
  }

  public void saveRecord(String data) {
    RecordData recordData = new RecordData();
    recordData.setData(data);
    recordDataRepository.save(recordData);
  }
}
