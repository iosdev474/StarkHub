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

public class homePageController extends SceneManager implements Initializable {

    @FXML
    private JFXListView videoList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initScene();
        calculateTrending();
        Label heading = new Label("Home");
        heading.setFont(new Font("Arial", 18));
        videoList.getItems().add(heading);
        if(trending!=null) {
            Iterator videoIterator = trending.videos.iterator();
            while (videoIterator.hasNext()) {
                Video video = (Video) videoIterator.next();
                VBox vBox = new VBox(new Label(video.fileName), new Label(video.channel), new Label("Likes: " + video.likes), new Label("Views: " + video.views));
                VBox vBox2 = new VBox(new Label(video.langauge), new Label(video.category), new Label("Date Recorded:: " + video.dateRecorded), new Label("Source IP: " + video.IP));
                Pane pane = new Pane();
                pane.setMinSize(100, 100);
                Pane pane2 = new Pane();
                pane2.setMinSize(100, 100);
                HBox hBox = new HBox(new ImageView("http://"+video.IP+":9999/"+video.thumbnailPath), pane, vBox, pane2, vBox2);
                hBox.setOnMouseClicked(event -> {
                    Main.videotoWatch = video;
                    changeScene(event, "mediaPlayer.fxml");
                });
                videoList.getItems().add(hBox);
            }
        }
    }

}
