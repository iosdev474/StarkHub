package StarkHub;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import sun.jvm.hotspot.opto.HaltNode;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class notificationViewController implements Initializable {
    @FXML
    private JFXListView notificationListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Creator creator = Main.getCreator();
        HBox hBox = new HBox(new Label("Clear"));
        hBox.setOnMouseClicked(event ->{
            creator.clearNotification();
            notificationListView.getItems().clear();
        });
        notificationListView.getItems().add(hBox);
        Iterator notificationIterator = creator.getNotificationIterator();
        while(notificationIterator.hasNext()){
            notificationListView.getItems().add(notificationIterator.next());
        }
    }
}
