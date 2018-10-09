package StarkHub;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class addChannelController extends SceneManager implements Initializable {

    @FXML
    private JFXButton menuButton1;

    @FXML
    private JFXButton menuButton2;

    @FXML
    private AnchorPane pane3;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane pane1;

    @FXML
    private JFXTextField channelNameTextField;

    @FXML
    private JFXTextArea descriptionField;

    /**
     * set all frame visible to false
     */
    public void setFrameFalse(){        //todo avoid redundancy
        menuButton1.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        menuButton2.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        menuButton1.setTextFill(Color.WHITE);
        menuButton2.setTextFill(Color.WHITE);
        pane1.setVisible(false);
        pane3.setVisible(false);
    }


    /**
     * Set Pane1 visible and rest not visible
     * @param event
     */
    @FXML
    public void menuButton1OnClickAction(ActionEvent event){
        setFrameFalse();
        menuButton1.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        menuButton1.setTextFill(Color.BLACK);
        pane1.setVisible(true);
    }

    /**
     * Set Pane2 visible and rest not visible
     * @param event
     */
    @FXML
    public void menuButton2OnClickAction(ActionEvent event){
        setFrameFalse();
        menuButton2.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        menuButton2.setTextFill(Color.BLACK);
        pane3.setVisible(true);
    }

    /**
     * Add channel to <code>creator and netManager</code>
     * after verifying entered password
     * @param event
     */
    @FXML
    void checkAndAddChannel(ActionEvent event) {

        if(channelNameTextField.getText().isEmpty()){
            menuButton1OnClickAction(event);
            return;
        }
        if(descriptionField.getText().isEmpty()){
            menuButton1OnClickAction(event);
            return;
        }



        PasswordHandler p = new PasswordHandler();
        if(p.checkPassword(passwordField.getText())){
            try{
                Channel channel = creator.addChannel(new Channel(channelNameTextField.getText(),descriptionField.getText()));
                netManager.addChanneltoList(channel);
                /*
                UNCOMMENT IF YOU ALLOW BLANK CHANNELS ON NETWORK
                Set<Channel> t = new HashSet<Channel>();
                t.add(channel);
                Packet packet = new Packet(0,t);
                netManager.generatePacket(packet);
                 */
            }catch (Exception e){
                System.err.println("Creator is null");
            }
            creator.save();
            System.out.println("Saved Creator");

            changeScene(event,"HomePage.fxml");
        }
        else
        {
            System.err.println("Incorrect Password");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setFrameFalse();
        initScene();
        menuButton1.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        menuButton1.setTextFill(Color.BLACK);
        pane1.setVisible(true);
    }
}
