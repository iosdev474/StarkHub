package StarkHub;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class channelDashboardController extends SceneManager implements Initializable {

    Channel tempchannel;
    Video tempvideo;

    @FXML
    private JFXListView myVideoList;

    @FXML
    private JFXTextField searchTextField;

    @FXML
    private ListView myChannelList;

    @FXML
    private JFXSpinner rating;

    @FXML
    private Label subscriptionLabel;

    @FXML
    private Label viewLabel;

    @FXML
    private Label likesLabel;

    @FXML
    private Label commentsLabel;

    @FXML
    private ListView commentsList;

    @FXML
    private Pane channelPane;

    @FXML
    private Pane videoPane;

    /**
     * add channel Controller
     * @param event
     */
    @FXML
    void addChannel(ActionEvent event){
        changeScene(event,"AddChannel.fxml");
    }

    /**
     * remove channel
     * @param event
     */
    @FXML
    void removeChannel(ActionEvent event){
        creator.channels.remove(tempchannel);
    }

    /**
     * load video
     * @param event
     */
    @FXML
    void viewVideo(ActionEvent event){
        changeScene(event,"mediaPlayer.fxml");
    }

    @FXML
    void removeVideo(ActionEvent event){
        Iterator channelIterator = creator.channels.iterator();
        while(channelIterator.hasNext()){
            Channel channel = (Channel) channelIterator.next();
            if(channel.getName().equals(tempvideo.channel)){
                channel.removeVideo(tempvideo);
                System.out.println("Video Removed");
                break;
            }
        }
    }

    @FXML
    void search(ActionEvent event) {
        if (!searchTextField.getText().isEmpty()) {
            Main.search = searchTextField.getText();
            Parent home_parent = null;
            try {
                home_parent = FXMLLoader.load(getClass().getResource("searchPage.fxml"));
                Scene Home = new Scene(home_parent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(Home);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void showVideo(Channel channel){
        Label heading = new Label("Comments");
        heading.setFont(new Font("Arial", 18));
        commentsList.getItems().add(heading);
        Iterator videoIterator = channel.getVideoIterator();
        while(videoIterator.hasNext()){
            Video video = (Video) videoIterator.next();
            VBox vBox = new VBox(new Label(video.fileName), new Label(video.channel), new Label("Likes: " + video.likes), new Label("Views: " + video.views));
            VBox vBox2 = new VBox(new Label(video.langauge), new Label(video.category), new Label("Date Recorded:: " + video.dateRecorded), new Label("Source IP: " + video.IP));
            Pane pane = new Pane();
            pane.setMinSize(50, 50);
            Pane pane2 = new Pane();
            pane2.setMinSize(50, 50);
            HBox hBox = new HBox(new ImageView("http://"+video.IP+":9999/"+video.thumbnailPath), pane, vBox, pane2, vBox2);
            hBox.setOnMouseClicked(event -> {
                tempvideo = video;
                channelPane.setVisible(false);
                videoPane.setVisible(true);
                viewLabel.setText(video.views+"");
                likesLabel.setText(video.likes+"");
                commentsLabel.setText(video.comments.size()+"");
                Iterator commentIterator = video.getCommentsIterator();
                while(commentIterator.hasNext()){
                    commentsList.getItems().add(commentIterator.next());
                }
            });
            myVideoList.getItems().add(hBox);
            rating.setProgress(((double)video.likes/video.views));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initScene();

        Label heading = new Label("My Channels");
        heading.setFont(new Font("Arial", 18));
        myChannelList.getItems().add(heading);
        heading = new Label("My Videos");
        heading.setFont(new Font("Arial", 18));
        myVideoList.getItems().add(heading);
        Iterator channelIterator = creator.channels.iterator();
        while(channelIterator.hasNext()){
            Channel channel = (Channel)channelIterator.next();
            Label name = new Label(channel.getName());
            Pane pane = new Pane();
            HBox hBox = new HBox(name,pane);
            hBox.setOnMouseClicked( e -> {
                tempchannel = channel;
                subscriptionLabel.setText(channel.getSubscribers()+"");
                videoPane.setVisible(false);
                channelPane.setVisible(true);
                showVideo(channel);
            });
            myChannelList.getItems().add(hBox);
        }
    }
}
