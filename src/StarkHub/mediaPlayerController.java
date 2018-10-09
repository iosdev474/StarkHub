package StarkHub;

import com.jfoenix.controls.JFXButton;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class mediaPlayerController extends SceneManager implements Initializable {
    @FXML
    private MediaView mediaView;

    @FXML
    private JFXButton play;

    @FXML
    private JFXButton stop;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider timeSlider;

    @FXML
    private ListView comments;

    @FXML
    private TextField commentTextField;

    @FXML
    private JFXButton likeButton;

    @FXML
    private JFXButton subscribeButton;

    @FXML
    private Pane mediaPane;

    @FXML
    private Label errorlabel;

    int Try = 1;
    private String filePath;
    private MediaPlayer mediaPlayer;
    boolean liked=false;
    boolean subscribed=false;

    public void setPlayer(String path) throws IOException {
        filePath =  path;
        System.out.println(filePath);
        if(filePath != null) {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
            mediaPlayer.setOnError(new Runnable() {
                @Override
                public void run() {
                    if (Try == 1) {
                        if (Main.videotoWatch.IP2 == null) {
                            errorlabel.setText("Video Unavailable");
                            return;
                        }
                    }
                    if(Try == 3)
                    Try++;
                    if (Main.videotoWatch.IP2 == null) {
                        errorlabel.setText("Video Unavailable");
                    }
                    else {
                        try {
                            if(Try == 2)
                                setPlayer("http://" + Main.videotoWatch.IP2 + ":9999/" + Main.videotoWatch.videoPath);
                            else if(Try == 3)
                                setPlayer("http://" + Main.videotoWatch.IP2 + ":9999/" + Main.videotoWatch.videoPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            InvalidationListener resizeMediaView = observable -> {
                mediaView.setFitWidth(mediaPane.getWidth());
                mediaView.setFitHeight(mediaPane.getHeight());
                Bounds actualVideoSize = mediaView.getLayoutBounds();
                mediaView.setX(((mediaView.getFitWidth() - actualVideoSize.getWidth()) / 2)-6);
                mediaView.setY((mediaView.getFitHeight() - actualVideoSize.getHeight()) / 2);
            };
            mediaPane.heightProperty().addListener(resizeMediaView);
            mediaPane.widthProperty().addListener(resizeMediaView);

            volumeSlider.setValue(mediaPlayer.getVolume() * 100);
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlider.getValue()/100);
                }
            });

            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    timeSlider.setValue(newValue.toSeconds());
                    timeSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
                }
            });

            timeSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
                }
            });

            timeSlider.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
                @Override
                public void handle(MouseDragEvent event) {
                    mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
                }
            });

        }
    }

    @FXML
    void PAUSE(ActionEvent event) {
        mediaPlayer.pause();
    }

    @FXML
    void PLAY(ActionEvent event) {
        mediaPlayer.play();
    }

    @FXML
    void STOP(ActionEvent event) {
        mediaPlayer.stop();
    }

    /**
     * likes a video by connecting to host server
     * @param event
     */
    @FXML
    public void like(ActionEvent event){
        if(!liked) {
            try {
                Packet packet = new Packet(0, Main.videotoWatch, true);
                Socket socket = new Socket(Main.videotoWatch.IP, 8080);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream.writeObject(packet);
                System.out.println("Liked");
                Iterator playlistIterator = Main.getCreator().playlists.iterator();
                while (playlistIterator.hasNext()) {
                    Playlist playlist = (Playlist) playlistIterator.next();
                    if (playlist.getName().equals("Liked Videos")) {
                        playlist.addVideo(Main.videotoWatch);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(Main.videotoWatch.likes);
            creator.addHistory("You liked " + Main.videotoWatch.fileName + " by " + Main.videotoWatch.channel);
            likeButton.setText("\uD83D\uDC4D Liked");
            liked = true;
        }
        else{
            try {
                Packet packet = new Packet(0, Main.videotoWatch, false);
                Socket socket = new Socket(Main.videotoWatch.IP, 8080);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream.writeObject(packet);
                System.out.println("removeLiked");
                Iterator playlistIterator = Main.getCreator().playlists.iterator();
                while (playlistIterator.hasNext()) {
                    Playlist playlist = (Playlist) playlistIterator.next();
                    if (playlist.getName().equals("Liked Videos")) {
                        playlist.removeVideo(Main.videotoWatch);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            liked=false;
            likeButton.setText("\uD83D\uDC4D Like");
        }
    }

    /**
     * add the video in its watchLater playlist
     * @param event
     */
    @FXML
    public void addwatchlater(ActionEvent event){
        Iterator playlistIterator = Main.getCreator().playlists.iterator();
        while (playlistIterator.hasNext()){
            Playlist playlist = (Playlist) playlistIterator.next();
            if(playlist.getName().equals("Watch Later")){
                playlist.addVideo(Main.videotoWatch);
                System.out.println("Added video to watchlater");
                break;
            }
        }
    }

    @FXML
    public void subscribe(ActionEvent event) {
        if (subscribed) {
            subscribed=false;
            Iterator channelIterator = creator.getSubscriptionIterator();
            while(channelIterator.hasNext()){
                Channel channel = (Channel) channelIterator.next();
                if(channel==null)
                    continue;
                Iterator videoIterator = channel.getVideoIterator();
                while(videoIterator.hasNext()){
                    Video video = (Video) videoIterator.next();
                    if(video.fileName.equals(Main.videotoWatch.fileName)){
                        creator.unsubscribe(channel);
                        break;
                    }
                }
            }
            subscribeButton.setText("Subscribe");
        }
        else {
            subscribed=true;
            subscribeButton.setText("Subscribed");
            Iterator channelIterator = netManager.getChannelListIterator();
            while(channelIterator.hasNext()){
                Channel channel = (Channel) channelIterator.next();
                Iterator videoIterator = channel.getVideoIterator();
                while(videoIterator.hasNext()){
                    Video video = (Video) videoIterator.next();
                    if(video.fileName.equals(Main.videotoWatch.fileName)){
                        creator.subscribe(channel);
                        System.out.println("Subscribed");
                        break;
                    }
                }
            }
            creator.subscribe(Main.channeltoWatch);
        }
    }

    @FXML
    public void postComment(ActionEvent event){
        if(commentTextField.getText().isEmpty()){
            System.out.println("Enter a text");
            return;
        }
        try {
            Packet packet = new Packet(0,Main.videotoWatch,commentTextField.getText());
            Socket socket = new Socket(Main.videotoWatch.IP,8080);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(packet);
            System.out.println("Liked");
            Iterator playlistIterator = Main.getCreator().playlists.iterator();
            while (playlistIterator.hasNext()){
                Playlist playlist = (Playlist) playlistIterator.next();
                if(playlist.getName().equals("Liked Videos")){
                    playlist.addVideo(Main.videotoWatch);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        creator.addHistory("You commented on "+Main.videotoWatch.fileName+" by "+Main.videotoWatch.channel);
        comments.getItems().add(commentTextField.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initScene();
        Iterator playlistIterator = Main.getCreator().playlists.iterator();
        while (playlistIterator.hasNext()){
            Playlist playlist = (Playlist) playlistIterator.next();
            if(playlist.getName().equals("Liked Videos")){
                Iterator videoIterator = playlist.videos.iterator();
                while(videoIterator.hasNext()){
                    Video video = (Video) videoIterator.next();
                    if(video.fileName.equals(Main.videotoWatch.fileName)){
                        likeButton.setText("\uD83D\uDC4D Liked");
                        liked=true;
                        break;
                    }
                }
                break;
            }
        }

        try {
            Packet packet = new Packet(0,Main.videotoWatch);
            Socket socket = new Socket(Main.videotoWatch.IP,8080);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(packet);
            System.out.println("Viewed");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator subscriptions = creator.getSubscriptionIterator();
        while (subscriptions.hasNext()) {
            Channel channel = (Channel) subscriptions.next();
            if(channel==null)
                continue;
            if(channel.getName().equals(Main.videotoWatch.channel)){
                subscribeButton.setText("Subscribed");
                subscribed=true;
            }
        }

        creator.addHistory("You watched "+Main.videotoWatch.fileName+" by "+Main.videotoWatch.channel);
        Iterator playlistIterator1 = creator.getPlaylistIterator();
        while(playlistIterator1.hasNext()){
            Playlist playlist = (Playlist) playlistIterator1.next();
            if(playlist.getName().equals("History")){
                playlist.addVideo(Main.videotoWatch);
                System.out.println("Video added to History");
                break;
            }
        }
        Main.videotoWatch.print();
        try {
            setPlayer("http://"+ Main.videotoWatch.IP+":9999/"+Main.videotoWatch.videoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterator commentsIterator = Main.videotoWatch.getCommentsIterator();
        Label heading = new Label("Comments");
        heading.setFont(new Font("Arial", 18));
        comments.getItems().add(heading);
        while(commentsIterator.hasNext()){
            comments.getItems().add(commentsIterator.next());
        }



    }
}
