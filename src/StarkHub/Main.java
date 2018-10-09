package StarkHub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
        primaryStage.setTitle("StarkHub");
        primaryStage.setScene(new Scene(root, 427, 477));
        primaryStage.show();
    }

    /**
     * private creator getter and setter methods.
     */
    private static Creator creator;
    public static Creator getCreator(){ return creator; }
    public static void setCreator(Creator c){ creator = c; }

    /**
     * private NetworkManager getter and setter methods.
     */
    private static NetworkManager netManager;
    public static NetworkManager getNetManager(){ return netManager; }
    public static void setNetManager(NetworkManager n){ netManager = n; }

    public static String search;
    public static Video videotoWatch;
    public static Playlist playlisttoWatch;
    public static Channel channeltoWatch;

    public static void main(String[] args) {
        launch(args);
    }

}
