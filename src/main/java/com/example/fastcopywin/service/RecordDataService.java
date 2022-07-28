package com.example.fastcopywin.service;

import com.example.fastcopywin.model.RecordData;
import com.example.fastcopywin.repository.RecordDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class RecordDataService {
  private static Logger log = Logger.getLogger(RecordDataService.class.toString());

  @Autowired
  RecordDataRepository recordDataRepository;

  @Autowired

  private String fileName = "fast_copy_win_data_sundayli.txt";

  public List<String> getRecordDataTopN(int n) {

    RecordData recordData = new RecordData();
    recordData.setId(1);
    recordData.setData("xxx");
    recordDataRepository.save(recordData);
    String data = getAllDataByfilePath(getCurrentRunningPath());
    List<String> dataList = new ArrayList<>();
    String[] arr = data.split("___");
    for (int i = arr.length - 1; i >= 0 && n >= 0; i--, n--) {
      dataList.add(arr[i]);
    }
    return dataList;
  }

  private String getAllDataByfilePath(String path) {
    StringBuilder builder = new StringBuilder();
    try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
      reader.lines().forEach(builder::append);
    } catch (Exception e) {
      log.warning("get data fail.");
    }
    return builder.toString();
  }

  public void saveRecord(String data) {
    try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getCurrentRunningPath(), true)))) {
      writer.write(data + "___");
    } catch (Exception e) {
      log.warning("save fail.");
    }
  }

  private String getCurrentRunningPath() {
    return System.getProperty("user.dir") + File.separator + fileName;
  }
}
