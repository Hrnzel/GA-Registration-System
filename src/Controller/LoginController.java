/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Dialog.Dialog;
import Model.User;
import SQL.UserDataAccessor;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author IT
 */
public class LoginController implements Initializable {

    UserDataAccessor users = new UserDataAccessor();
    
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private String username;
    @FXML private Label exit;
    @FXML private Pane topBar;
    @FXML private Pane body;
    double mouseX, mouseY;
    
    int ctr = 3;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void onExit(){
        Platform.exit();
    }
    
    @FXML
    private void minimize(){
        Stage stage = (Stage)body.getScene().getWindow();
        System.out.println("here!");
        stage.setIconified(true);
    }
    
    @FXML
    private void onDragged(MouseEvent evt){
        Stage stage = (Stage)body.getScene().getWindow();
        stage.setX(evt.getScreenX()-mouseX);
        stage.setY(evt.getScreenY()-mouseY);
    }
    
    @FXML
    private void onPressed(MouseEvent evt){
        Stage stage = (Stage)topBar.getScene().getWindow();
        mouseX = evt.getSceneX();
        mouseY = evt.getSceneY();
    }
    
    public void checkCredentials(String username, String password){
        if(username.isEmpty()){
            if(password.isEmpty()){
                Dialog.errorMessage("Please type your username and password","Login");
            }else{
                Dialog.errorMessage("Please type your username", "Login");
            }
        }else if(password.isEmpty()){
            Dialog.errorMessage("Please type your password", "Login");
        }else{
            List<User> userList = users.getAccountFromDB(username);
            if(!userList.isEmpty()){
                if(password.equals(userList.get(0).getPassword())){
                    ChooseAppController chooseApp;
                    Stage stage = (Stage)body.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Choose App.fxml"));
                    Parent root = null;
                    try{
                        root = loader.load();
                    }catch(Exception e){
                        Dialog.errorMessage(e.toString(), "Login", "Error loading fxml.");
                    }
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    
                    chooseApp = loader.getController();
                    chooseApp.setUsername(username);
                    chooseApp.setSceneOnCenter();
                }else{
                    ctr--;
                    if(ctr==0){
                        Dialog.errorMessage("Account permanent banned!", "Login");
                        ctr = 3;
                    }else{
                        Dialog.errorMessage("You only have " + ctr + " try/tries left", "Login", "Your password is wrong!");
                    }
                }
            }
        }
    }
    
    public void login(ActionEvent evt){
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        checkCredentials(username, password);
    }
    
    public void setSceneOnCenter(){
        Stage stage = (Stage)topBar.getScene().getWindow();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
    
}
