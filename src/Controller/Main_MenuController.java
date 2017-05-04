package Controller;

import Dialog.Dialog;
import com.github.sarxos.webcam.Webcam;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main_MenuController implements Initializable {
    
    InformationController info = null;
    MembersListController member = null;
    @FXML private Label exit;
    @FXML private Pane topBar;
    @FXML private Pane body;
    double mouseX, mouseY;
    @FXML private Button cabanas,marilao,cruz,apalit,
            dinalupihan, main, balanga, baliuag, starosa,
            sanmiguel,gapan;
    @FXML private Label cabanas1,marilao1,cruz1,apalit1,
            dinalupihan1, main1, balanga1, baliuag1, starosa1,
            sanmiguel1,gapan1;
    private Webcam webcam;
    private String username;
    
    public void setUsername(String username){
        this.username = username;
    }
    
    @FXML
    private void logout(ActionEvent ae){
        LoginController login;
        
        Stage stage = (Stage)body.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
        Parent root = null;
        try{
            root = loader.load();
        }catch(Exception e){
            Dialog.errorMessage(e.toString(), "Photo Uploading App", "Error loading fxml.");
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        login = loader.getController();
        
        login.setSceneOnCenter();
    }
    
    @FXML
    private void handleButtonAction(ActionEvent e){
        Stage stage = (Stage)cabanas.getScene().getWindow();
        FXMLLoader loader = null;
        String name = "";
        
        if(e.getSource()==cabanas){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Cabanas";
        }
        else if(e.getSource()==main){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Main";
        }
        else if(e.getSource()==marilao){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Marilao";
        }
        else if(e.getSource()==apalit){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Apalit";
        }
        else if(e.getSource()==cruz){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Cruz";
        }
        else if(e.getSource()==gapan){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Gapan";
        }
        else if(e.getSource()==dinalupihan){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Dinalupihan";
        }
        else if(e.getSource()==balanga){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Balanga";
        }
        else if(e.getSource()==baliuag){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Baliuag";
        }
        else if(e.getSource()==starosa){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Sta Rosa";
        }
        else if(e.getSource()==sanmiguel){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "San Miguel";
        }
        if(webcamIsConnected()){
            if(webcam==null){
                Dialog.warningMessage("Please select a webcam!","Main Menu");
                selectWebCam();
            }else{
                Parent root = null;
                try{
                    root = loader.load();
                }catch(Exception ex){
                    Dialog.errorMessage(ex.toString(), "Main Menu", "Error loading fxml.");
                }

                Scene scene = new Scene(root);

                member = loader.getController();

                stage.setScene(scene);
                stage.show();

                member.setUsername(username);
                member.setBranch(name);
                member.setSceneOnCenter();
                member.setDataToTable(name);
                member.setWebCam(getWebcam());
            }
        }else{
            Dialog.errorMessage("This app needs a webcam to work!","Photo Uploading App","Please connect your webcam!");
        }
        
    }
    
    @FXML
    private void selectBranch(MouseEvent e){
        Stage stage = (Stage)cabanas.getScene().getWindow();
        FXMLLoader loader = null;
        String name = "";
        
        if(e.getSource()==cabanas1){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Cabanas";
        }
        else if(e.getSource()==main1){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Main";
        }
        else if(e.getSource()==marilao1){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Marilao";
        }
        else if(e.getSource()==apalit1){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Apalit";
        }
        else if(e.getSource()==cruz1){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Cruz";
        }
        else if(e.getSource()==gapan1){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Gapan";
        }
        else if(e.getSource()==dinalupihan1){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Dinalupihan";
        }
        else if(e.getSource()==balanga1){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Balanga";
        }
        else if(e.getSource()==baliuag1){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Baliuag";
        }
        else if(e.getSource()==starosa1){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "Sta Rosa";
        }
        else if(e.getSource()==sanmiguel1){
            loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
            name = "San Miguel";
        }
        if(webcamIsConnected()){
            if(webcam==null){
                Dialog.warningMessage("Please select a webcam!","Main Menu");
                selectWebCam();
            }else{
                Parent root = null;
                try{
                    root = loader.load();
                }catch(Exception ex){
                    Dialog.errorMessage(ex.toString(), "Main Menu", "Error loading fxml.");
                }

                Scene scene = new Scene(root);

                member = loader.getController();

                stage.setScene(scene);
                stage.show();

                member.setUsername(username);
                member.setBranch(name);
                member.setSceneOnCenter();
                member.setDataToTable(name);
                member.setWebCam(getWebcam());
            }
        }else{
            Dialog.errorMessage("This app needs a webcam to work!","Photo Uploading App","Please connect your webcam!");
        }
        
    }
    
    public void setSceneOnCenter(){
        Stage stage = (Stage)topBar.getScene().getWindow();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
    
    public void switchToRegSys(){
        RegistrationMenuController regMenu;
        Stage stage = (Stage)body.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Registration Menu.fxml"));
        Parent root = null;
        try{
            root = loader.load();
        }catch(Exception e){
            Dialog.errorMessage(e.toString(), "Registration System", "Error loading fxml.");
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        regMenu = loader.getController();

        regMenu.setUsername(this.username);
        regMenu.setSceneOnCenter();
        if(webcam!=null){
            regMenu.setWebcam(getWebcam());
        }
        regMenu.checkUser();
    }
    
    @FXML
    private void onExit(){
        int i = Dialog.confirmMessage("Are you sure you want to exit?", "Photo Uploading App");
        if(i==1){
            Platform.exit();
        }
    }
    
    @FXML
    private void close(ActionEvent e){
        Platform.exit();
    }
    
    @FXML
    private void minimize(){
        Stage stage = (Stage)body.getScene().getWindow();
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
    
    public boolean webcamIsConnected(){
        return !(Webcam.getWebcams().isEmpty());
    }
    
    public void selectWebCam(){
        ObservableList<WebCamInfo> options = FXCollections.observableArrayList();
        int webcamctr = 0;
        for(Webcam wc : Webcam.getWebcams()){
            WebCamInfo wci = new WebCamInfo();
            wci.setWebCamIndex(webcamctr);
            wci.setWebCamName(wc.getName());
            options.add(wci);
            webcamctr++;
        }
        ChoiceDialog<WebCamInfo> dialog = new ChoiceDialog<>();
        dialog.setContentText("Choose your camera");
        dialog.setTitle("Member's Information");
        dialog.setHeaderText(null);
        dialog.getItems().addAll(options);

        Optional<WebCamInfo> result = dialog.showAndWait();
        if(result.isPresent()){
            webcam = Webcam.getWebcams().get(result.get().getWebCamIndex());
        }
    }
    
    public void handleWindowShownEvent(){
        selectWebCam();
    }
    
    public Webcam getWebcam(){
        return this.webcam;
    }
    
    public void setWebcam(Webcam cam){
        this.webcam = cam;
    }
    
    class WebCamInfo
    {
            private String webCamName ;
            private int webCamIndex ;

            public String getWebCamName() {
                    return webCamName;
            }
            public void setWebCamName(String webCamName) {
                    this.webCamName = webCamName;
            }
            public int getWebCamIndex() {
                    return webCamIndex;
            }
            public void setWebCamIndex(int webCamIndex) {
                    this.webCamIndex = webCamIndex;
            }

            @Override
            public String toString() {
                    return webCamName;
         }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }  
    
    public void about(ActionEvent e){
        try {
            Stage stage = (Stage)body.getScene().getWindow();
            
            Stage secondStage = new Stage();
            secondStage.initStyle(StageStyle.UNDECORATED);
            secondStage.initOwner(stage);
            secondStage.initModality(Modality.WINDOW_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/About.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            secondStage.setScene(scene);
            
            secondStage.show();
        } catch (IOException ex) {
            Dialog.errorMessage(ex.toString(),"Main Menu","Error loading FXML");
        }
    }
    
    public void backupDB(ActionEvent e){
        String dbName = "lkbp";
        String dbUser = "root";
        String dbPass = "1528";
        String executeCmd="";
        String path="";
        
        Stage stage = (Stage)body.getScene().getWindow();
        
        FileChooser fileChoose = new FileChooser();
        fileChoose.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("SQL File", "*.sql")
        );
        File file = fileChoose.showSaveDialog(stage);
        if(file!=null){
            path = file.getAbsolutePath();

            executeCmd = "C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump -h 10.3.6.2 -P 3306 -u " + dbUser + " -p" + dbPass + " " + dbName + " -r " + path;
            try{
                Process runtimeProcess =Runtime.getRuntime().exec(executeCmd);

                int processComplete = runtimeProcess.waitFor();
                if(processComplete == 0){
                    Dialog.infoMessage("Backup taken successfully!", "Backup Database");
                }else{
                    Dialog.errorMessage("Could not take backup", "Backup Database");
                }
            }catch(IOException | InterruptedException ex) {
                Dialog.errorMessage(ex.toString(), "Backup Database");
            }
        }
    }
}
