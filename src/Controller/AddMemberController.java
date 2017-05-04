package Controller;

import Dialog.Dialog;
import Model.NewMember;
import SQL.NewMemberDataAccessor;
import com.github.sarxos.webcam.Webcam;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AddMemberController implements Initializable {

    MembersListController member;
    NewMemberDataAccessor newM = new NewMemberDataAccessor();
    
    @FXML private Label branch;
    
    @FXML private Label exit;
    @FXML private Pane topBar;
    @FXML private Pane body;
    double mouseX, mouseY;
    
    private String branchName, branchCode, username;
    
    DateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
    
    @FXML private TextField txtFirstName, txtMiddleName, txtLastName,
            txtBranchCode, txtAccount;
    
    private Webcam webcam;
    
    Timer timer;
    TimerTask task;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    private void onExit(){
        int i = Dialog.confirmMessage("Are you sure you want to exit?", "Photo Uploading App");
        if(i==1){
            Platform.exit();
            if(timer!=null){
                timer.cancel();
                timer.purge();
            }
        }
    }
    
    public void setUsername(String username){
        this.username = username;
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
    
    @FXML
    private void onBackPressed(MouseEvent e){
        Stage stage = (Stage)body.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
        Parent root = null;
        try{
            root = loader.load();
        }catch(Exception ex){
            Dialog.errorMessage(ex.toString(), "Member's List", "Error loading fxml.");
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        member = loader.getController();
        
        member.setUsername(username);
        member.setSceneOnCenter();
        member.setBranch(this.branchName);
        member.setSceneOnCenter();
        member.setDataToTable(this.branchName);
        member.setWebCam(webcam);
        if(timer!=null){
            timer.cancel();
            timer.purge();
        }
    }
    
    public void setCamera(Webcam cam){
        this.webcam = cam;
    }
    
    public void setBranch(String branchName){
        this.branchName = branchName;
        branch.setText(branchName + " Branch");
        
        if(branchName.equalsIgnoreCase("Main")){
            branchCode = "001";
        }else if(branchName.equalsIgnoreCase("Cruz")){
            branchCode = "002";
        }else if(branchName.equalsIgnoreCase("Baliuag")){
            branchCode = "003";
        }else if(branchName.equalsIgnoreCase("San Miguel")){
            branchCode = "004";
        }else if(branchName.equalsIgnoreCase("Gapan")){
            branchCode = "005";
        }else if(branchName.equalsIgnoreCase("Balanga")){
            branchCode = "006";
        }else if(branchName.equalsIgnoreCase("Dinalupihan")){
            branchCode = "007";
        }else if(branchName.equalsIgnoreCase("Cabanas")){
            branchCode = "008";
        }else if(branchName.equalsIgnoreCase("Sta Rosa")){
            branchCode = "009";
        }else if(branchName.equalsIgnoreCase("Apalit")){
            branchCode = "010";
        }else if(branchName.equalsIgnoreCase("Marilao")){
            branchCode = "011";
        }else if(branchName.equalsIgnoreCase("Central Fund")){
            branchCode = "012";
        }else if(branchName.equalsIgnoreCase("Corporate")){
            branchCode = "888";
        }
        
        txtBranchCode.setText(branchCode);
        
        timer = new Timer();
        
        final String code = branchCode;
        
        task = new TimerTask() {
            @Override
            public void run() {
                List<NewMember> list = newM.getNewMemberList("select * from "
                        + "tbl_new_member where account_no like '"+code+"%'");
                if(!list.isEmpty()){
                    String lastNumber = "";
                    int last = 0;
                    String account = list.get(list.size()-1).getAccountNo();
                    account = account.substring(3);
                    last = Integer.parseInt(account.trim());
                    last++;
                    account = String.format("%06d", last);
                    txtAccount.setText(account);
                }
            }
        };
        timer.schedule(task, 0, 1000);
        
    }
    
    public void addMemberAction(ActionEvent evt){
        String name = "";
        String first = txtFirstName.getText();
        String middle = txtMiddleName.getText();
        String last = txtLastName.getText();
        String account = txtAccount.getText();
        String code = txtBranchCode.getText();
        
        
        if(first.isEmpty()){
            Dialog.errorMessage("First Name cannot be blank!","Add Member");
        }else if(last.isEmpty()){
            Dialog.errorMessage("First Name cannot be blank!","Add Member");
        }else if(account.isEmpty()){
            Dialog.errorMessage("Account No. cannot be blank!","Add Member");
        }else{
            last = last.toUpperCase();
            first = first.toUpperCase();
            
            if(middle.isEmpty()){
                name = last + ", " + first;
            }else{
                middle = middle.toUpperCase();
                name = last + ", " + first + " " + middle;
            }
            if(account.length()>=6){
                account = code + account;
                addMember(name, account);
            }else{
                Dialog.errorMessage("Account No must be 9 or 13 digits!", "Add Member");
            }
        }
        
    }
    
    public void addMember(String name, String account){
        boolean success = newM.addMember(account, name, this.branchName);
        if(success){
            Dialog.infoMessage("Member successfully added!", "Add Member");
        }else{
            Dialog.errorMessage("Member failed to add", "Add Member");
        }
    }
    
}
