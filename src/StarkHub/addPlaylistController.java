package StarkHub;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class addPlaylistController extends SceneManager implements Initializable {

    private Channel tempChannel;

    @FXML
    private JFXListView mychannels;

    @FXML
    private JFXListView myplaylist;

    @FXML
    private JFXListView channelplaylist;

    @FXML
    private JFXTextField playlistname;

    /**
     * adds private playlist for creator
     * @param event
     */
    @FXML
    void addPrivate(ActionEvent event) {
        if(playlistname.getText().isEmpty()){
            System.out.println("Enter a playlist name");
            return;
        }
        creator.addPlaylist(new Playlist(playlistname.getText()));
        loadLists();
        creator.save();
    }

    /**
     * adds public playlist for channel
     * @param event
     */
    @FXML
    void addPublic(ActionEvent event) {
        if(playlistname.getText().isEmpty()){
            System.out.println("Enter a playlist name");
            return;
        }
        if(tempChannel==null){
            System.out.println("Choose a channel");
        }
        try{
            tempChannel.addPlaylist(new Playlist(playlistname.getText()));
        }catch (NullPointerException e){
            System.out.println("Choose a channel");
        }
        loadLists();
        creator.save();
    }

    /**
     * load playlistView
     */
    private void loadPlaylist(){
        Iterator playlistsIterator = tempChannel.getPlaylistsIterator();
        while(playlistsIterator.hasNext()){
            Playlist playlist = (Playlist) playlistsIterator.next();
            Button button = new Button(playlist.getName());
            button.setOnAction(event -> {
                Main.playlisttoWatch=playlist;
                changeScene(event,"playlistView.fxml");
            });
            channelplaylist.getItems().add(button);
        }
    }

    /**
     * load GUI
     */
    private void loadLists(){
        mychannels.getItems().clear();
        myplaylist.getItems().clear();
        channelplaylist.getItems().clear();
        Iterator channelIterator = creator.getChannelListIterator();
        if(channelIterator.hasNext()){
            Channel channel = (Channel) channelIterator.next();
            Label l1 = new Label(channel.getName());
            Button button = new Button("go");
            button.setOnAction(event -> {
                tempChannel = channel;
                loadPlaylist();
            });
            mychannels.getItems().add(new HBox(l1,button));
            tempChannel = channel;
            loadPlaylist();
        }
        while(channelIterator.hasNext()) {
            Channel channel = (Channel) channelIterator.next();
            Label l1 = new Label(channel.getName());
            Button button = new Button("go");
            button.setOnAction(event -> {
                tempChannel = channel;
                loadPlaylist();
            });
            mychannels.getItems().add(new HBox(l1,button));
        }
        Iterator playlistIterator = creator.getPlaylistIterator();
        while(playlistIterator.hasNext()){
            Playlist playlist = (Playlist)playlistIterator.next();
            Button button = new Button(playlist.getName());
            button.setOnAction(event -> {
                Main.playlisttoWatch=playlist;
                changeScene(event,"playlistView.fxml");
            });
            myplaylist.getItems().add(button);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initScene();
        loadLists();
    }
}
