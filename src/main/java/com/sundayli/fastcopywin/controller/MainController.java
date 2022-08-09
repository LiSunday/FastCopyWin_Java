package com.sundayli.fastcopywin.controller;

import com.sundayli.fastcopywin.Main;
import com.sundayli.fastcopywin.model.RecordData;
import com.sundayli.fastcopywin.model.eum.DataFormatEnum;
import com.sundayli.fastcopywin.service.RecordDataService;
import com.sundayli.fastcopywin.system.CustomClipboard;
import com.sundayli.fastcopywin.system.model.ClipboardData;
import com.sundayli.fastcopywin.util.ImageUtils;
import java.awt.datatransfer.DataFlavor;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainController extends BaseController implements Initializable {

  @FXML
  public ListView<Object> dataList;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // 刷新数据 & 强制将焦点放在第一项
    List<Object> list = new ArrayList<>();
    for (RecordData recordData : Main.CONTEXT.getBean(RecordDataService.class).getRecordDataTopN(10)) {
      Optional.ofNullable(recordDataToObj(recordData)).ifPresent(list::add);
    }
    ObservableList<Object> observableList = FXCollections.observableList(list);
    dataList.setItems(observableList);

    // 设置此页面 回车时的动作
    addAction(ActionTypeEnum.ENTER, dataArray -> {
      if (dataList.isFocused()) {
        Object data = dataList.getFocusModel().getFocusedItem();
        if (data instanceof String){
          ClipboardData clipboardData = new ClipboardData();
          clipboardData.setDataFlavor(DataFlavor.stringFlavor);
          clipboardData.setStrData((String) data);
          CustomClipboard.getInstance().coverClipboardData(clipboardData);
        } else if (data instanceof ImageView) {
          ImageView imageView = (ImageView) data;
          Image image = imageView.getImage();
          ClipboardData clipboardData = new ClipboardData();
          clipboardData.setDataFlavor(DataFlavor.imageFlavor);
          clipboardData.setBufferedImage(ImageUtils.toBufferedImage(image));
          CustomClipboard.getInstance().coverClipboardData(clipboardData);
        }
      }
    });
    addAction(ActionTypeEnum.SHOW, dataArray -> dataList.getSelectionModel().selectFirst());
    addAction(ActionTypeEnum.UPDATE, dataArray -> {
      Object obj = recordDataToObj((RecordData) dataArray[0]);
      if (obj != null) {
        observableList.add(0, obj);
        if (observableList.size() > 10) {
          observableList.remove(observableList.size() - 1);
        }
      }
    });
  }

  private Object recordDataToObj(RecordData recordData) {
    if (recordData == null) {
      return null;
    }
    if (recordData.getDataFormat() == DataFormatEnum.PLAIN_TEXT) {
      return recordData.getData();
    } else if (recordData.getDataFormat() == DataFormatEnum.IMAGE) {
      ImageView imageView = new ImageView(ImageUtils.toImage(recordData.getData()));
      double w = imageView.getImage().getWidth();
      double h = imageView.getImage().getHeight();
      // TODO: 2022/8/4 sundayli 后面抽成设置 这里用 40 是把显示区域做个缩放 图片太大不美观 也不方便
      imageView.setFitHeight(40);
      imageView.setFitWidth(w / h * 40);
      return imageView;
    }
    return null;
  }
}