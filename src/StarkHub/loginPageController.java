package StarkHub;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginPageController implements Initializable {

    NetworkManager networkManager;
    Creator creator;

    @FXML
    public JFXTextField ipTextField;

    @FXML
    public JFXPasswordField passwordTextField;

    @FXML
    public Label errorLabel;

    @FXML
    public void login(ActionEvent event){
        errorLabel.setText("");
        if(passwordTextField.getText().isEmpty()){
            errorLabel.setText("Please Enter Password");
            return;
        }
        if(ipTextField.getText().isEmpty()){
            errorLabel.setText("Please Enter IP");
            return;
        }
        PasswordHandler p = new PasswordHandler();
        if(p.checkPassword(passwordTextField.getText())){
            networkManager = new NetworkManager(ipTextField.getText());
            creator = (Creator)SaveAndLoadController.load(Creator.filename);
            if(creator==null){
                System.out.println("Creator is null");
                passwordTextField.setStyle("-jfx-unfocus-color: RED");
                errorLabel.setText("Please create a account");
                return;
            }
            if(networkManager==null){
                errorLabel.setText("Some internal error occurred. Please report it to the developer immediately");
            }
            Main.setNetManager(networkManager);
            Main.setCreator(creator);

            Parent home_parent = null;
            try {
                home_parent = FXMLLoader.load(getClass().getResource("homePage.fxml"));
                Scene Home = new Scene(home_parent,1280,720);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(Home);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            passwordTextField.setStyle("-jfx-unfocus-color: RED");
            errorLabel.setText("Wrong Password");
        }
    }

    @FXML
    public void createNewAccount(ActionEvent event){
        Creator creator = new Creator(System.getProperty("user.name"));
        try {
            new File("password.ser").delete();
            FileWriter writer = new FileWriter("password.ser", false);
            writer.write(SHA256Handler.getSha256(passwordTextField.getText()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        errorLabel.setText("Account created. Please press Login");
    }

    @FXML
    public void close(ActionEvent event){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
