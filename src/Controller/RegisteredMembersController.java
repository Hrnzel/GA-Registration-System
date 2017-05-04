package Controller;

import Dialog.Dialog;
import SQL.MemberDataAccessor;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RegisteredMembersController implements Initializable {

    
    MemberDataAccessor member = new MemberDataAccessor();
    
    RegistrationMenuController regMenu;
    
    @FXML private Label exit, back;
    @FXML private Pane topBar;
    @FXML private Pane body;
    double mouseX, mouseY;
    
    @FXML private Label lblMain, lblApalit, lblBalanga,
            lblBaliuag, lblCabanas, lblCruz, lblDinalupihan,
            lblGapan, lblMarilao, lblSanMiguel, lblStaRosa;
    
    @FXML private Button btnCabanas,btnMarilao,btnCruz,btnApalit,
            btnDinalupihan, btnMain, btnBalanga, btnBaliuag, btnStaRosa,
            btnSanMig,btnGapan;
    
    private String username;
    int ctr;
    @FXML private Label lblCounter;
    private Timeline timeline;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateRegisteredMembers();
    }
    
    public void updateRegisteredMembers(){
        timeline = new Timeline(
            new KeyFrame(Duration.seconds(0), (ActionEvent actionEvent) -> {
                lblCounter.setText(member.getRegisteredMembersCount());
                lblMain.setText(member.getRegisteredCountByBranch("Main"));
                lblApalit.setText(member.getRegisteredCountByBranch("Apalit"));
                lblBalanga.setText(member.getRegisteredCountByBranch("Balanga"));
                lblBaliuag.setText(member.getRegisteredCountByBranch("Baliuag"));
                lblCabanas.setText(member.getRegisteredCountByBranch("Cabanas"));
                lblCruz.setText(member.getRegisteredCountByBranch("Cruz"));
                lblDinalupihan.setText(member.getRegisteredCountByBranch("Dinalupihan"));
                lblGapan.setText(member.getRegisteredCountByBranch("Gapan"));
                lblMarilao.setText(member.getRegisteredCountByBranch("Marilao"));
                lblSanMiguel.setText(member.getRegisteredCountByBranch("San Miguel"));
                lblStaRosa.setText(member.getRegisteredCountByBranch("Sta Rosa"));
        }),
              new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    @FXML
    private void onExit(){
        timeline.stop();
        Stage stage = (Stage)body.getScene().getWindow();
        stage.close();
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
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public void setSceneOnCenter(){
        Stage stage = (Stage)topBar.getScene().getWindow();
//        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
//        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
//        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        stage.setFullScreen(true);
    }
    
    public void viewRegisteredMembers(ActionEvent evt){
        Stage stage = (Stage)body.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Branch Registered Members.fxml"));
        String name = "";
        
        if(evt.getSource()==btnMain){
            name = "Main";
        }else if(evt.getSource()==btnCabanas){
            name = "Cabanas";
        }else if(evt.getSource()==btnMarilao){
            name = "Marilao";
        }else if(evt.getSource()==btnCruz){
            name = "Cruz";
        }else if(evt.getSource()==btnApalit){
            name = "Apalit";
        }else if(evt.getSource()==btnDinalupihan){
            name = "Dinalupihan";
        }else if(evt.getSource()==btnBalanga){
            name = "Balanga";
        }else if(evt.getSource()==btnBaliuag){
            name = "Baliuag";
        }else if(evt.getSource()==btnStaRosa){
            name = "Sta Rosa";
        }else if(evt.getSource()==btnSanMig){
            name = "San Miguel";
        }else if(evt.getSource()==btnGapan){
            name = "Gapan";
        }
        
        Parent root = null;
        try{
            root = loader.load();
        }catch(Exception e){
            Dialog.errorMessage(e.toString(), "Registration System", "Error loading fxml.");
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        BranchRegisteredMembersController brm = loader.getController();
        
        brm.setSceneOnCenter();
        brm.setBranch(name);
        brm.setUsername(username);
        brm.setDataToTable(name);
    }
    
}
