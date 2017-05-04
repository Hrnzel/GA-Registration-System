package Controller;

import Dialog.Dialog;
import Model.Member;
import SQL.MemberDataAccessor;
import com.github.sarxos.webcam.Webcam;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MembersListController implements Initializable {

    Main_MenuController menu;
    AddMemberController add;
    MemberDataAccessor data = new MemberDataAccessor();
    InformationController info;
    
    @FXML private Label branch;
    @FXML private TextField txtSearch;
    @FXML private Pane body1;
    @FXML private Pane topBar1;
    @FXML private Label back;
    @FXML private TableView<Member> tblMember;
    @FXML private TableColumn<Member, String> tblId, tblName;
    double mouseX, mouseY;
    String branchName;
    ObservableList<Member> data1 = FXCollections.observableArrayList();
    private String username;
    
    private Webcam webcam;
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public Webcam getWebCam(){
        return this.webcam;
    }
    
    public void setWebCam(Webcam cam){
        this.webcam = cam;
    }
    
    @FXML
    private void onExit(){
        int i = Dialog.confirmMessage("Are you sure you want to exit?", "Photo Uploading App");
        if(i==1){
            Platform.exit();
        }
    }
    
    @FXML
    private void minimize(){
        Stage stage = (Stage)body1.getScene().getWindow();
        stage.setIconified(true);
    }
    
    @FXML
    private void onDragged(MouseEvent evt){
        Stage stage = (Stage)body1.getScene().getWindow();
        stage.setX(evt.getScreenX()-mouseX);
        stage.setY(evt.getScreenY()-mouseY);
    }
    
    @FXML
    private void onPressed(MouseEvent evt){
        Stage stage = (Stage)topBar1.getScene().getWindow();
        mouseX = evt.getSceneX();
        mouseY = evt.getSceneY();
    }
    
    @FXML
    private void onBackPressed(MouseEvent evt){
        Stage stage = (Stage)back.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Main_Menu.fxml"));
        Parent root = null;
        try{
            root = loader.load();
        }catch(Exception e){
            Dialog.errorMessage(e.toString(), "Member's List", "Error loading fxml.");
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        menu = loader.getController();
        
        menu.setUsername(this.username);
        menu.setSceneOnCenter();
        menu.setWebcam(this.webcam);
    }
    
    public void setBranch(String branchName){
        this.branchName = branchName;
        branch.setText(branchName + " Branch");
    }
    
    public void setSceneOnCenter(){
        Stage stage = (Stage)topBar1.getScene().getWindow();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
    
    public void setDataToTable(String branchName){
        
        String sql = "(select * "
                + "from member where branch like '"+branchName+"') UNION "
                + "(select * from tbl_new_member where branch like '"+branchName+"')";
        data1 = data.getMembersList(sql);
        tblMember.setItems(data1);
    }
    
    @FXML
    private void refreshTable(ActionEvent e){
        String sql = "(select * "
                + "from member where branch like '"+branchName+"') UNION "
                + "(select * from tbl_new_member where branch like '"+branchName+"')";
        data1 = data.getMembersList(sql);
        tblMember.setItems(data1);
    }
    
    public void selectDataInTable(ActionEvent e){
        int i = -1;
        i = tblMember.getSelectionModel().getSelectedIndex();
        if(i!=-1){
            Member tableData = tblMember.getSelectionModel().getSelectedItem();
            Stage stage = (Stage)body1.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Information.fxml"));
            Parent root = null;
            try{
                root = loader.load();
            }catch(Exception ex){
                Dialog.errorMessage(ex.toString(), "Member's List", "Error loading fxml.");
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
            info = loader.getController();
            
            info.setUsername(this.username);
            info.setSceneOnCenter();
            info.setBranchName(branchName);
            info.setCIFNo(tableData.getID());
            info.setName(tableData.getName());
            info.setPicToImageView();
            info.setCamera(getWebCam());
        }else{
            Dialog.errorMessage("Please select a member", "Members List");
        }
    }
    
    @FXML
    private void search(ActionEvent e){
        String sql = "";
        String keyWord = txtSearch.getText();
        int d = 0;
        String[] range = {"0","1","2","3","4","5","6","7","8","9"};
        if(!keyWord.trim().isEmpty()){
            for(String a: range){
                if(keyWord.contains(a)){
                    d = 1;
                    break;
                }
            }
            if(d==1){
                sql = "(select * "
                        + "from member "
                        + "where Account_no like '%"+keyWord+"%' and branch like '"+branchName+"') "
                        + "UNION "
                        + "(select * "
                        + "from tbl_new_member "
                        + "where Account_no like '%"+keyWord+"%' and branch like '"+branchName+"')";
            }else{
                sql = "(select * "
                        + "from member "
                        + "where branch like '"+branchName+"' and name like '%"+keyWord+"%')"
                        + " UNION "
                        + "(select * "
                        + "from tbl_new_member "
                        + "where branch like '"+branchName+"' and name like '%"+keyWord+"%')";
            }
            if(!sql.isEmpty()){
                data1 = data.getMembersList(sql);
                tblMember.setItems(data1);
            }
        }else{
            Dialog.errorMessage("Please type what you want to search", "Members List");
        }
    }
    
    public void goToAddMember(ActionEvent e){
        Stage stage = (Stage)body1.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Add Member.fxml"));
        Parent root = null;
        try{
            root = loader.load();
        }catch(Exception ex){
            Dialog.errorMessage(ex.toString(), "Member's List", "Error loading fxml.");
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        add = loader.getController();
        
        add.setUsername(username);
        add.setBranch(branchName);
        add.setCamera(webcam);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tblName.setCellValueFactory(new PropertyValueFactory<>("Name"));
    }
}
