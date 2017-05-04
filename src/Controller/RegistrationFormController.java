package Controller;

import Dialog.Dialog;
import Model.Member;
import Model.ShareCapital;
import Model.User;
import SQL.MemberDataAccessor;
import SQL.NewMemberDataAccessor;
import SQL.ShareCapitalAccessor;
import SQL.UserDataAccessor;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RegistrationFormController implements Initializable {

    MemberDataAccessor dataMember = new MemberDataAccessor();
    NewMemberDataAccessor newM = new NewMemberDataAccessor();
    UserDataAccessor user = new UserDataAccessor();
    ShareCapitalAccessor share = new ShareCapitalAccessor();
    RegistrationMenuController regMenu;
    DecimalFormat formatter = new DecimalFormat("#,##0.00");
    DateFormat timeFormat = new SimpleDateFormat("HH:mm aa");
    DateFormat dateFormat = new SimpleDateFormat("MMM/dd/yyyy");
    
    @FXML private TextField txtSearch;
    @FXML private Label lblName, lblCIF;
    @FXML private Label exit, back;
    @FXML private Pane topBar;
    @FXML private Pane body;
    @FXML private Label lblStatus, lblComShare, lblPrefShare,
            lblFixDep,lblTotal,GrandPrize;
    @FXML private Label lblRegistered;
    @FXML private Label lblTime, lblDate;
    @FXML private Label lblID, lblUser;
    @FXML private TableView<Member> tblMembers;
    @FXML private TableColumn<Member, String> tblId, tblName;
    @FXML private ImageView img;
    private String username;
    
    MemberDataAccessor data = new MemberDataAccessor();
    ObservableList<Member> data1 = FXCollections.observableArrayList();
    
    private int ctr;
    double mouseX, mouseY;
    private String branch, CIF;
    @FXML private Label branchName;
    private Timeline timeline;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tblName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tblMembers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedMember();
                setPicToImageView();
            }
        });
        updateTime();
    }    
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public void updateTime(){
        timeline = new Timeline(
            new KeyFrame(Duration.seconds(0), (ActionEvent actionEvent) -> {
                Calendar time = Calendar.getInstance();
                SimpleDateFormat timeFormat1 = new SimpleDateFormat("hh:mm:ss aa");
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("EEEE, MMM d, YYYY");
                lblTime.setText(timeFormat1.format(time.getTime()));
                lblDate.setText(dateFormat1.format(time.getTime()));
        }),
              new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    public void setSceneOnCenter(){
        Stage stage = (Stage)topBar.getScene().getWindow();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
    
    public void setBranchName(String branch){
        this.branch = branch;
        branchName.setText(branch + " Branch");
    }
    
    public void setName(String name){
        lblName.setText(name);
    }
    
    public void setStatus(String status){
        if(status==null || status.equalsIgnoreCase("No delay")){
            lblStatus.setStyle("-fx-text-fill: #0d906d;");
            lblStatus.setText("MIGS");
        }else{
            lblStatus.setStyle("-fx-text-fill: red;");
            lblStatus.setText("NON-MIGS");
        }
    }
    
    public void setRegistration(String register){
        lblRegistered.setText(register);
        if(register.equalsIgnoreCase("not yet registered") ||
                register.equalsIgnoreCase("no selected member")){
            lblRegistered.setStyle("-fx-text-fill: red;");
        }else{
            lblRegistered.setStyle("-fx-text-fill: #0d906d;");
        }
    }
    
    public void setCIFNo(String CIF){
        this.CIF = CIF;
        lblCIF.setText(CIF);
    }
    
    public void setComShare(String comShare){
        lblComShare.setText(comShare);
    }
    
    public void setPrefShare(String prefShare){
        lblPrefShare.setText(prefShare);
    }
    
    public void setFixDep(String fixDep){
        lblFixDep.setText(fixDep);
    }
    
    public void setTotal(String total){
        lblTotal.setText(total);
    }
    
    public void isQualified(double total){
        if(total>=15000 && lblStatus.getText().equalsIgnoreCase("migs")){
            GrandPrize.setStyle("-fx-text-fill: #0d906d;");
            GrandPrize.setText("QUALIFIED");
        }else{
            GrandPrize.setStyle("-fx-text-fill: red;");
            GrandPrize.setText("NOT QUALIFIED");
        }
    }

    public void setDataToTable(){
        String sql = "select account_no, name, status "
                + "from member where branch like '"+branch+"'";
        data1 = data.getMembersList(sql);
        tblMembers.setItems(data1);
    }
    
    public void selectMember(MouseEvent e){
        selectedMember();
    }
    
    public void selectedMember(){
        Member tableData = tblMembers.getSelectionModel().getSelectedItem();
        
        if(tableData!=null){
            setCIFNo(tableData.getID());
            setName(tableData.getName());
        }
        List<ShareCapital> shares = null;
        if(CIF!=null){
            shares = share.getShareCapital(CIF);
        }
        
        Double c = 0.0;
        Double p = 0.0;
        Double f = 0.0;
        Double t = 0.0;
        if(shares!=null){
            if(!shares.isEmpty()){
                c = Double.parseDouble(shares.get(0).getComShare().replace(",",""));
                p = Double.parseDouble(shares.get(0).getPrefShare().replace(",",""));
                f = Double.parseDouble(shares.get(0).getFixDep().replace(",",""));
                t = Double.parseDouble(shares.get(0).getTotal().replace(",",""));
            }
        }
        
        setComShare(formatter.format(c));
        setPrefShare(formatter.format(p));
        setFixDep(formatter.format(f));
        setTotal(formatter.format(t));
        if(shares!=null){ 
            if(!shares.isEmpty()){
                setStatus(shares.get(0).getStatus());
            }else{
                setStatus("NON-MIGS");
            }
        }
        String sql = "select account_no, name, status "
                + "from member where branch like '"+branch+"' and account_no like '"+CIF+"'";
        List<Member> members = dataMember.getMembersList(sql);
        
        if(!members.isEmpty()){
            setRegistration(members.get(0).getStatus());
        }else{
            setRegistration("No selected member");
        }
        isQualified(t);
    }
    
    public void setInitialInfo(){
        tblMembers.getSelectionModel().selectFirst();
        Member tableData = tblMembers.getSelectionModel().getSelectedItem();
        
        if(tableData!=null){
            setCIFNo(tableData.getID());
            setName(tableData.getName());
        }
        List<ShareCapital> shares = null;
        if(CIF!=null){
            shares = share.getShareCapital(CIF);
        }
        
        Double c = 0.0;
        Double p = 0.0;
        Double f = 0.0;
        Double t = 0.0;
        if(shares!=null){
            if(!shares.isEmpty()){
                c = Double.parseDouble(shares.get(0).getComShare().replace(",",""));
                p = Double.parseDouble(shares.get(0).getPrefShare().replace(",",""));
                f = Double.parseDouble(shares.get(0).getFixDep().replace(",",""));
                t = Double.parseDouble(shares.get(0).getTotal().replace(",",""));
            }
        }
        
        setComShare(formatter.format(c));
        setPrefShare(formatter.format(p));
        setFixDep(formatter.format(f));
        setTotal(formatter.format(t));
        if(shares!=null){ 
            if(!shares.isEmpty()){
                setStatus(shares.get(0).getStatus());
            }else{
                setStatus("NON-MIGS");
            }
        }
        
        String sql = "select account_no, name, status "
                + "from member where branch like '"+branch+"' and account_no like '"+CIF+"'";
        List<Member> members = dataMember.getMembersList(sql);
        if(!members.isEmpty()){
            setRegistration(members.get(0).getStatus());
        }else{
            setRegistration("No selected member");
        }
        isQualified(t);
        
    }
    
    public void setUserData(){
        List<User> users;
        
        users = user.getAccountFromDB(this.username);
        
        if(!users.isEmpty()){
            lblID.setText(users.get(0).getUserID());
            String fname = users.get(0).getFirstName();
            String lname = users.get(0).getLastName();
            lblUser.setText(fname + " " + lname);
        }
    }
    
    public void refreshTable(ActionEvent e){
        txtSearch.setText("");
        setDataToTable();
        setInitialInfo();
    }
    
    @FXML
    private void onExit(){
        int i = Dialog.confirmMessage("Are you sure you want to exit?", "Registration System");
        if(i==1){
            Platform.exit();
        }
    }
    
    @FXML
    private void minimize(){
        Stage stage = (Stage)body.getScene().getWindow();
        System.out.println("here!");
        stage.setIconified(true);
    }
    
    public void onBackPressed(MouseEvent evt){
        int i = Dialog.confirmMessage("Are you sure you want to go back?", "Registration System");
        if(i==1){
            timeline.stop();
            Stage stage = (Stage)back.getScene().getWindow();
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
            regMenu.checkUser();
        }
    }
    
    public void search(ActionEvent e){
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
                        + "from member where Account_no like '%"+keyWord+"%' and branch like '"+branch+"'";
            }else{
                sql = "select * "
                        + "from member where branch like '"+branch+"' and name like '%"+keyWord+"%'";
            }
            if(!sql.isEmpty()){
                data1 = data.getMembersList(sql);
                tblMembers.setItems(data1);
            }
        }else{
            Dialog.errorMessage("Please type what you want to search", "Members List");
        }
    }
    
    public void register(ActionEvent e){
        int i = Dialog.confirmMessage("Are you sure you want to register this member?", "Registration System");
        if(i==1){
            if(!lblRegistered.getText().equalsIgnoreCase("registered")){
                boolean registered = data.registerMember(CIF,username);
                if(registered==true){
                    Dialog.infoMessage("Member successfully registered!", "Registration System");
                    selectedMember();
                }else{
                    Dialog.errorMessage("Member failed to register.", "Registration System");
                }
            }else{
                Dialog.errorMessage("This member is already registered!", "Registration System");
            }
        }
    }
    
    public void setPicToImageView(){
        if(CIF.length()==9){
            //Check if member have picture
            if(newM.havePic(CIF)){
                String picPath = newM.getPicPath(CIF);
                try{
                    Image asd = new Image("file:" + picPath);
                    img.setImage(asd);
                }catch(Exception e){
                    Dialog.errorMessage(e.toString(), "Member's Information");
                }
            }else{
                img.setImage(null);
            }
        }else{
            //Check if member have picture
            if(dataMember.havePic(CIF)){
                String picPath = dataMember.getPicPath(CIF);
                try{
                    Image asd = new Image("file:" + picPath);
                    img.setImage(asd);
                }catch(Exception e){
                    Dialog.errorMessage(e.toString(), "Member's Information");
                }
            }else{
                img.setImage(null);
            }
        }
        
    }
}