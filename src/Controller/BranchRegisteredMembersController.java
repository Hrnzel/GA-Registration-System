package Controller;

import Dialog.Dialog;
import Model.Member;
import SQL.MemberDataAccessor;
import java.net.URL;
import java.util.ResourceBundle;
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

public class BranchRegisteredMembersController implements Initializable {

    ObservableList<Member> data1 = FXCollections.observableArrayList();
    MemberDataAccessor data = new MemberDataAccessor();
    
    @FXML private Label exit, back;
    @FXML private Pane topBar;
    @FXML private Pane body;
    double mouseX, mouseY;
    private String username;
    
    @FXML private TableView<Member> tblMember;
    @FXML private TableColumn<Member, String> tblId, tblName;
    
    @FXML private TextField txtSearch;
    
    private String branchName;
    @FXML private Label branchNN;
    
    public void setUsername(String username) {
        this.username = username;
        System.out.println("Hello!");
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tblName.setCellValueFactory(new PropertyValueFactory<>("Name"));
    }    
    
    @FXML
    private void onExit(){
        Stage stage = (Stage)body.getScene().getWindow();
        stage.close();
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
    
    public void onBackPressed(MouseEvent evt){
        RegisteredMembersController regMem;
        Stage stage = (Stage)body.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Registered Members.fxml"));
        Parent root = null;
        try{
            root = loader.load();
        }catch(Exception ex){
            Dialog.errorMessage(ex.toString(), "Main Menu", "Error loading fxml.");
        }
        
        regMem = loader.getController();
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        regMem.setUsername(username);
        regMem.setSceneOnCenter();
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
                sql = "select * "
                        + "from member "
                        + "where Account_no like '%"+keyWord+"%' and branch like '"+branchName+" and status like "
                        + "'Registered'";
            }else{
                sql = "select * "
                        + "from member "
                        + "where branch like '"+branchName+"' and name like '%"+keyWord+"%' and status like "
                        + "'Registered'";
            }
            if(!sql.isEmpty()){
                data1 = data.getMembersList(sql);
                tblMember.setItems(data1);
            }
        }else{
            Dialog.errorMessage("Please type what you want to search", "Members List");
        }
    }
    
    public void setBranch(String branchName){
        this.branchName = branchName;
        branchNN.setText(branchName + " Branch");
    }
    
    public void setSceneOnCenter(){
        Stage stage = (Stage)topBar.getScene().getWindow();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
    
    public void setDataToTable(String branchName){
        String sql = "select * "
                + "from member where branch like '"+branchName+"' and status like 'Registered'";
        data1 = data.getMembersList(sql);
        tblMember.setItems(data1);
        
    }
    
    
}
