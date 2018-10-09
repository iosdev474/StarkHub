package StarkHub;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;

/**
 * contains all event handling functions required in every controller file
 */
public class SceneManager {

    protected NetworkManager netManager;
    protected Creator creator;
    protected Playlist trending;

    @FXML
    private JFXTextField searchTextField;

    @FXML
    private JFXListView libraryListView;

    @FXML
    private JFXListView subscriptionListView;

    @FXML
    private JFXButton notificationButton;


    void changeScene(Event event, String scene){
        Parent home_parent = null;
        try {
            home_parent = FXMLLoader.load(getClass().getResource(scene));
            Scene Home = new Scene(home_parent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(Home);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void home(ActionEvent event) {
        changeScene(event,"homePage.fxml");
    }

    public void calculateTrending(){
        if(netManager.isChannelNull()){
            return;
        }
        int count=20;
        trending = new Playlist("Trending");
        Iterator channelListIterator = netManager.getChannelListIterator();
        while(channelListIterator.hasNext()){
            Channel channel = (Channel) channelListIterator.next();
            Iterator videoIterator = channel.getVideoIterator();
            while(videoIterator.hasNext()){
                Video video = (Video) videoIterator.next();
                if(count==10){
                    Iterator videoIterator1 = trending.videos.iterator();
                    Video leastLikedVideo=video;
                    while(videoIterator1.hasNext()){
                        Video video1 = (Video) videoIterator1.next();
                        if(leastLikedVideo.likes>video1.likes){
                            leastLikedVideo=video1;
                        }
                        if(leastLikedVideo!=video){
                            trending.removeVideo(leastLikedVideo);
                            trending.addVideo(video);
                        }
                    }
                }
                else {
                    trending.addVideo(video);
                    count++;
                }
            }
        }
    }

    @FXML
    public void trending(ActionEvent event) {
        calculateTrending();
        Main.playlisttoWatch=trending;
        changeScene(event,"playlistView.fxml");
    }

    @FXML
    void playlist(ActionEvent event) {
        changeScene(event,"addPlaylist.fxml");
    }

    @FXML
    void history(ActionEvent event) {
        Iterator playlistIterator = Main.getCreator().playlists.iterator();
        while (playlistIterator.hasNext()){
            Playlist playlist = (Playlist) playlistIterator.next();
            if(playlist.getName().equals("History")){
                Main.playlisttoWatch = playlist;
                break;
            }
        }
        changeScene(event,"playlistView.fxml");
    }

    @FXML
    void subscriptions(ActionEvent event){

    }

    @FXML
    void paidMembership(ActionEvent event){
        creator.addNotification("Thanks for purchasing");
        creator.isPremium=true;
    }

    @FXML
    void notification(ActionEvent event){
        creator.resetNewNotifications();
        notificationButton.setText("0");
        Parent root1 = null;
        try {
            root1 = (Parent) FXMLLoader.load(getClass().getResource("notificationView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    public void watchlater(ActionEvent event){
        creator.addNotification(" channel updated!");
        Iterator playlistIterator = Main.getCreator().playlists.iterator();
        while (playlistIterator.hasNext()){
            Playlist playlist = (Playlist) playlistIterator.next();
            if(playlist.getName().equals("Watch Later")){
                Main.playlisttoWatch = playlist;
                break;
            }
        }
        changeScene(event,"playlistView.fxml");
    }

    @FXML
    public void likedVideos(ActionEvent event){
        Iterator playlistIterator = Main.getCreator().playlists.iterator();
        while (playlistIterator.hasNext()){
            Playlist playlist = (Playlist) playlistIterator.next();
            if(playlist.getName().equals("Liked Videos")){
                Main.playlisttoWatch = playlist;
                break;
            }
        }
        changeScene(event,"playlistView.fxml");
    }

    @FXML
    void search(ActionEvent event){
        if(!searchTextField.getText().isEmpty()){
            Main.search = searchTextField.getText();
            changeScene(event,"searchPage.fxml");
        }
    }

    @FXML
    public void addVideo(ActionEvent event) {           //upload button action
        changeScene(event, "addVideo.fxml");
    }

    @FXML
    public void mychannel(ActionEvent event) {
        changeScene(event, "channelDashboard.fxml");
    }

    @FXML
    public void back(ActionEvent event){
        changeScene(event,"HomePage.fxml");
    }

    public void getObjects(){
        netManager = Main.getNetManager();
        creator = Main.getCreator();
    }

    void initScene(){
        getObjects();
        if(libraryListView==null){
            System.out.println("LibraryListView is null");
            return;
        }
        if(subscriptionListView==null){
            System.out.println("subscriptionListView is null");
            return;
        }
        if(notificationButton!=null)
            notificationButton.setText(creator.getNewNotifications()+"");
        Iterator playlistIterator = creator.getPlaylistIterator();


        Label heading = new Label("My Playlists");
        heading.setFont(new Font("Arial", 18));
        libraryListView.getItems().add(heading);
        while(playlistIterator.hasNext()){
            Playlist playlist = (Playlist)playlistIterator.next();
            if(playlist.getName().equals("Watch Later") || playlist.getName().equals("Liked Videos") || playlist.getName().equals("History")){
                continue;
            }
            Label label = new Label(playlist.getName());
            HBox hBox = new HBox(label);
            hBox.setOnMouseClicked(event -> {
                Main.playlisttoWatch = playlist;
                changeScene(event,"playlistView.fxml");
            });
            libraryListView.getItems().add(hBox);
        }
        Iterator subscriptions = creator.getSubscriptionIterator();


        heading = new Label("My Subscriptions");
        heading.setFont(new Font("Arial", 18));
        subscriptionListView.getItems().add(heading);
        while(subscriptions.hasNext()){
            Channel channel = (Channel)subscriptions.next();
            if(channel==null)
                continue;
            HBox hBox = new HBox(new Label(channel.getName()));
            hBox.setOnMouseClicked(event -> {
                Main.channeltoWatch = channel;
                changeScene(event,"userDashboard.fxml");
            });
            subscriptionListView.getItems().add(hBox);
        }
    }

}
