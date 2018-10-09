package StarkHub;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class playlistViewController extends SceneManager implements Initializable {

    @FXML
    private JFXListView playlistvideolist;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initScene();
        Label heading = new Label(Main.playlisttoWatch.getName());
        heading.setFont(new Font("Arial", 18));
        playlistvideolist.getItems().add(heading);
        Iterator videoIterator = Main.playlisttoWatch.videos.iterator();
        while(videoIterator.hasNext()){
            Video video = (Video) videoIterator.next();
            VBox vBox = new VBox(new Label(video.fileName), new Label(video.channel), new Label("Likes: " + video.likes), new Label("Views: " + video.views));
            VBox vBox2 = new VBox(new Label(video.langauge), new Label(video.category), new Label("Likes: " + video.dateRecorded), new Label("Views: " + video.IP));
            Pane pane = new Pane();
            pane.setMinSize(100, 100);
            Pane pane2 = new Pane();
            pane2.setMinSize(100, 100);
            HBox hBox = new HBox(new ImageView("http://"+video.IP+":9999/"+video.thumbnailPath), pane, vBox, pane2, vBox2);
            hBox.setOnMouseClicked(event -> {
                Main.videotoWatch = video;
                changeScene(event, "mediaPlayer.fxml");
            });
            playlistvideolist.getItems().add(hBox);
        }
    }
}
