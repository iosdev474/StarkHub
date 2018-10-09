package StarkHub;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import javafx.event.ActionEvent;
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

public class userDashboardController extends SceneManager implements Initializable {

    private Video tempVideo;

    @FXML
    private JFXListView myVideoList;

    @FXML
    private JFXListView myPlaylistList;

    @FXML
    private JFXSpinner uptimeSpinner;

    @FXML
    private Label subscriptionLabel;

    @FXML
    private Label viewLabel;

    @FXML
    private Label likesLabel;


    @FXML
    void viewVideo(ActionEvent event){
        changeScene(event,"mediaPlayer.fxml");
    }

    @FXML
    public void subscribe(ActionEvent event){
        creator.subscribe(Main.channeltoWatch);
    }

    void loadVideos(Playlist playlist){
        Iterator videoIterator;
        if(playlist==null){
            videoIterator = Main.channeltoWatch.getVideoIterator();
        }
        else {
            videoIterator = playlist.videos.iterator();
        }
        while (videoIterator.hasNext()) {
            Video video = (Video) videoIterator.next();
            VBox vBox = new VBox(new Label(video.fileName), new Label(video.channel), new Label("Likes: " + video.likes), new Label("Views: " + video.views));
            VBox vBox2 = new VBox(new Label(video.langauge), new Label(video.category), new Label("Date Recorded:: " + video.dateRecorded), new Label("Source IP: " + video.IP));
            Pane pane = new Pane();
            pane.setMinSize(50, 50);
            Pane pane2 = new Pane();
            pane2.setMinSize(50, 50);
            HBox hBox = new HBox(new ImageView("http://" + video.IP + ":9999/" + video.thumbnailPath), pane, vBox, pane2, vBox2);
            hBox.setOnMouseClicked(event -> {
                Main.videotoWatch = video;
                viewLabel.setText(video.views + "");
                likesLabel.setText(video.likes + "");
                uptimeSpinner.setProgress(((double) video.likes / video.views));
            });
            myVideoList.getItems().add(hBox);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initScene();
        Label heading = new Label("Playlists");
        heading.setFont(new Font("Arial", 18));
        myPlaylistList.getItems().add(heading);
        heading = new Label("Videos");
        heading.setFont(new Font("Arial", 18));
        myVideoList.getItems().add(heading);
        loadVideos(null);
        Channel channel = Main.channeltoWatch;
        subscriptionLabel.setText(channel.getSubscribers()+"");
        Iterator playlistIterator = channel.getPlaylistsIterator();
        while(playlistIterator.hasNext()){
            Playlist playlist = (Playlist) playlistIterator.next();
            HBox hBox = new HBox(new Label(playlist.getName()));
            hBox.setOnMouseClicked(event ->{
                loadVideos(playlist);
            });
            myPlaylistList.getItems().add(hBox);
        }
    }
}
