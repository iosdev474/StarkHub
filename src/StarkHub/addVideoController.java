package StarkHub;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

public class addVideoController extends SceneManager implements Initializable {

    @FXML
    private JFXTextField videoNameTextField;

    @FXML
    private JFXTextArea descriptionField;

    @FXML
    private JFXChipView tagChipView;

    @FXML
    private JFXComboBox categoryComboBox;

    @FXML
    private JFXComboBox videoLangaugeComboBox;

    @FXML
    private JFXDatePicker dateRecordedDatePicker;

    @FXML
    private JFXComboBox channelNameComboBox;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private AnchorPane pane1;

    @FXML
    private AnchorPane pane2;

    @FXML
    private AnchorPane pane3;

    @FXML
    private JFXButton menuButton1;

    @FXML
    private JFXButton menuButton2;

    @FXML
    private JFXButton menuButton3;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField videoPathTextField;

    @FXML
    private JFXTextField thumbnailPathTextField;

    /**
     * set all frame visible to false
     */
    private void setFrameFalse(){
        menuButton1.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        menuButton2.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        menuButton3.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        menuButton1.setTextFill(Color.WHITE);
        menuButton2.setTextFill(Color.WHITE);
        menuButton3.setTextFill(Color.WHITE);
        pane1.setVisible(false);
        pane2.setVisible(false);
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
        System.out.println("1");
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
        pane2.setVisible(true);
        System.out.println("2");
    }

    /**
     * Set Pane3 visible and rest not visible
     * @param event
     */
    @FXML
    public void menuButton3OnClickAction(ActionEvent event){
        setFrameFalse();
        menuButton3.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        menuButton3.setTextFill(Color.BLACK);
        pane3.setVisible(true);
        System.out.println("3");
    }

    /**
     * Add video to <code>creator's channel</code>
     * after verifying entered password
     * @param event
     */
    @FXML
    public void checkAndAddVideo(ActionEvent event){

        if(videoNameTextField.getText().isEmpty() || descriptionField.getText().isEmpty() || videoPathTextField.getText().isEmpty() || channelNameComboBox.getSelectionModel().isEmpty()){
            menuButton1OnClickAction(event);
            return;
        }
        if(tagChipView.getChips().isEmpty() || videoLangaugeComboBox.getSelectionModel().isEmpty() || categoryComboBox.getSelectionModel().isEmpty() || dateRecordedDatePicker.getValue()==null){
            menuButton2OnClickAction(event);
            return;
        }

        PasswordHandler p = new PasswordHandler();
        System.out.println("ppppppp:"+p);
        if(p.checkPassword(passwordField.getText())){
            Video video;
            Set<String> tags = new HashSet<String>();
            Iterator it = tagChipView.getChips().iterator();
            while(it.hasNext())
                tags.add((String)it.next());
            if(creator.isPremium){
                String IP2="",IP3="";
                int count=3;
                Iterator channelListIterator = netManager.getChannelListIterator();
                while(channelListIterator.hasNext()){
                    Channel channel = (Channel) channelListIterator.next();
                    Iterator videoIterator = channel.getVideoIterator();
                    while(videoIterator.hasNext()){
                        Video vid = (Video) videoIterator.next();
                        IP2=IP3;
                        IP3=vid.IP;
                        count--;
                        if(count==0)
                            break;
                    }
                }
                video = new Video(channelNameComboBox.getValue().toString(),videoPathTextField.getText().substring(videoPathTextField.getText().indexOf('/')+1),videoNameTextField.getText(),thumbnailPathTextField.getText().substring(thumbnailPathTextField.getText().indexOf('/')+1),tags,descriptionField.getText(),videoLangaugeComboBox.getValue().toString(),categoryComboBox.getValue().toString(),dateRecordedDatePicker.getValue(),3,netManager.getMyIP(),IP2,IP3);
            }
            else{
                video = new Video(channelNameComboBox.getValue().toString(),videoPathTextField.getText().substring(videoPathTextField.getText().indexOf('/')+1),videoNameTextField.getText(),thumbnailPathTextField.getText().substring(thumbnailPathTextField.getText().indexOf('/')+1),tags,descriptionField.getText(),videoLangaugeComboBox.getValue().toString(),categoryComboBox.getValue().toString(),dateRecordedDatePicker.getValue(),1,netManager.getMyIP());
            }
            Channel channel = creator.addVideo(video);
            SaveAndLoadController.save(Creator.filename,creator);
            Set<Channel> t = new HashSet<Channel>();
            t.add(channel);

            Packet packet = new Packet(0,t);
            netManager.generatePacket(packet);
            System.out.println("Generated Channel List");
            netManager.addChanneltoList(channel);
            netManager.save();
            System.out.println("Saved Channel list");

            changeScene(event,"HomePage.fxml");
        }
        else
        {
            System.out.println("Incorrect Password");
        }
    }

    public String choosefile(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Choose a video with .mp4 Extension.","*.mp4");
        fileChooser.setSelectedExtensionFilter(null);
        File file = fileChooser.showOpenDialog(null);
        String path = file.toURI().toString();
        return path;
    }

    @FXML
    public void chooseVideo(ActionEvent event){
        videoPathTextField.setText(choosefile());
    }

    @FXML
    public void chooseThumbnail(ActionEvent event){
        thumbnailPathTextField.setText(choosefile());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setFrameFalse();
        initScene();
        videoLangaugeComboBox.getItems().add("English");
        videoLangaugeComboBox.getItems().add("Hindi");
        videoLangaugeComboBox.getItems().add("Gujarati");
        videoLangaugeComboBox.getItems().add("Urdu");
        videoLangaugeComboBox.getItems().add("Others");
        categoryComboBox.getItems().add("Education");
        categoryComboBox.getItems().add("Entertainment");
        categoryComboBox.getItems().add("Others");
        try {
            for (Channel ch : creator.channels) {
                channelNameComboBox.getItems().add(ch.getName());
            }
        }catch (Exception e){
            System.err.println("Create a channel");
        }
        menuButton1.setTextFill(Color.BLACK);
        menuButton1.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane1.setVisible(true);
    }
}
