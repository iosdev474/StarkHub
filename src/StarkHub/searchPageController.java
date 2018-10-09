package StarkHub;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class searchPageController extends SceneManager implements Initializable {

    String search;

    @FXML
    private JFXListView searchList;

    @FXML
    public JFXTextField searchTextField;

    boolean checkInTag(Video video,String search){
        Iterator tagIterator = video.getTagIterator();
        while(tagIterator.hasNext()){
            if(((String)tagIterator.next()).contains(search)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initScene();
        search = Main.search;
        NetworkManager networkManager = Main.getNetManager();
        if(search==null)
            System.out.println("1");
        if(searchTextField==null)
            System.out.println("2");
        searchTextField.setText(search);
        if(!networkManager.isChannelNull()){
            Iterator channelIterator = networkManager.getChannelListIterator();
            while(channelIterator.hasNext()){
                Channel channel = (Channel)channelIterator.next();
                if(channel.getName().equals(search)){
                    Label l1 = new Label(channel.getName());
                    Label l2 = new Label("CHANNEL: ");
                    HBox hBox = new HBox();
                    hBox.getChildren().addAll(l1,l2);
                    hBox.setOnMouseClicked(event -> {
                        Main.channeltoWatch = channel;
                        changeScene(event,"userDashboard.fxml");
                    });
                    searchList.getItems().add(hBox);
                }
                Iterator videoIterator = channel.getVideoIterator();
                while(videoIterator.hasNext()){
                    Video video = (Video)videoIterator.next();
                    if(video.fileName.contains(search) || checkInTag(video,search)){
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
                        searchList.getItems().add(hBox);
                    }
                }
            }
        }
    }
}
