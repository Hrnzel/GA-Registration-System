/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Dialog.Dialog;
import SQL.MemberDataAccessor;
import SQL.NewMemberDataAccessor;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

public final class InformationController implements Initializable {

    NewMemberDataAccessor newM;
    MemberDataAccessor dataMember;
    MembersListController member;
    @FXML private Label back;
    @FXML private Label branchName;
    @FXML private Label lblCIF;
    @FXML private Label lblName;
    @FXML private Pane body1;
    @FXML private Pane topBar1;
    @FXML private ImageView bgImg;
    private String username;
    double mouseX, mouseY;
    String CIF;
    String Name;
    String strBranchName;
    
    private boolean isSaved = false;
    private boolean pictureIsTaken = false;
    
    @FXML private Button btnStartCam;
    @FXML private Button btnStopCam;
    @FXML private Button btnTakePic;
    @FXML private Button btnSavePic;
    
    @FXML private ImageView img;
    @FXML private Webcam webcam;
    @FXML private Pane imgHolder;
    private BufferedImage grabbedImage;
    private final ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    private boolean stopCamera = false;
    
    Stage stage;
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public void setCamera(Webcam cam){
        this.webcam = cam;
    }
    
    protected void startWebcamStream() {
        stopCamera  = false;

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                while (!stopCamera) {
                    try {
                        if ((grabbedImage = webcam.getImage()) != null) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    final Image mainImage = SwingFXUtils.toFXImage(grabbedImage, null);
                                    imageProperty.set(mainImage);
                                }
                            });
                            grabbedImage.flush();
                        }
                    } catch (Exception e) {} finally {}
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        img.imageProperty().bind(imageProperty);
    }
    
    public void startCam(ActionEvent e){
        stopCamera = false;
        
        if(!webcam.isOpen()){
            webcam.setCustomViewSizes(new Dimension[] {WebcamResolution.VGA.getSize()});
            webcam.setViewSize(WebcamResolution.VGA.getSize());
            webcam.open();
        }
        
        double height = imgHolder.getHeight();
        double width = imgHolder.getWidth();
        img.setFitHeight(height);
        img.setFitWidth(width);
        img.prefHeight(height);
        img.prefWidth(width);
        img.setPreserveRatio(true);
        startWebcamStream();
        
        if(btnStartCam.getText().equals("Start Camera") || btnStartCam.getText().equals("Update Picture")){
            btnStartCam.setDisable(true);
            btnTakePic.setDisable(false);
            btnStopCam.setDisable(false);
        }else{
            btnStartCam.setDisable(true);
            btnSavePic.setVisible(false);
            btnTakePic.setVisible(true);
            btnTakePic.setDisable(false);
        }
        btnStartCam.setText("Another shot?");
    }
    
    public void takePic(ActionEvent e){
        stopCamera = true;
        btnStartCam.setDisable(false);
        btnTakePic.setDisable(true);
        btnTakePic.setVisible(false);
        btnSavePic.setVisible(true);
        pictureIsTaken = true;
    }
    
    public void savePicture(){
        String path = "\\Images";
        path = "Z:" + "\\" + path + "\\" + CIF + ".jpg";
        boolean success = false;
        
        boolean update = false;
        
        //Update image path to DB
        if(CIF.length()==9){
            update = newM.updateDBPicPath(CIF, path);
        }else{
            update = dataMember.updateDBPicPath(CIF, path);
        }
        
        //set path to make Directory
        File dir = new File(path);
        
        //check if updating succeed
        try{
            if(update==true){
                grabbedImage = Scalr.resize(grabbedImage, Method.ULTRA_QUALITY, 320, 240);
                if(!dir.exists()){
                    dir.mkdir();
                    success = ImageIO.write(grabbedImage, "JPG", new File(path));
                }else{
                    success = ImageIO.write(grabbedImage, "JPG", new File(path));
                }
                if(success==true){
                    Dialog.infoMessage("Picture has been saved to: \n\n " + path,"Member's Information");
                }else{
                    Dialog.errorMessage("Picture not save.", "Member's Information");
                }
            }else{
                Dialog.errorMessage("Picture not save.", "Member's Information");
            }
        }catch(IOException ex){
            Dialog.errorMessage(ex.toString(), "Member's Information", "Error uploading photo");
        }
        isSaved = success;
        btnStartCam.setDisable(false);
        btnSavePic.setVisible(false);
        btnTakePic.setDisable(true);
        btnTakePic.setVisible(true);
        btnStopCam.setDisable(false);
    }
    
    public void savePic(ActionEvent e){
        savePicture();
    }
    
    public void stopCam(ActionEvent e){
        stopCamera = true;
        webcam.close();
        btnStartCam.setText("Start Camera");
        btnStartCam.setDisable(false);
        btnStopCam.setDisable(true);
        btnTakePic.setVisible(true);
        btnTakePic.setDisable(true);
        btnSavePic.setVisible(false);
        img.imageProperty().unbind();
        setPicToImageView();
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
    
    public void setSceneOnCenter(){
        Stage stage = (Stage)topBar1.getScene().getWindow();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
    
    public void loadMemberList(){
        Stage stage = (Stage)back.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MembersList.fxml"));
        Parent root = null;
        try{
            root = loader.load();
        }catch(Exception e){
            Dialog.errorMessage(e.toString(), "Member's Information", "Error loading fxml.");
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        member = loader.getController();
        
        member.setUsername(this.username);
        member.setSceneOnCenter();
        member.setBranch(strBranchName);
        member.setSceneOnCenter();
        member.setDataToTable(strBranchName);
        member.setWebCam(this.webcam);
    }
    
    @FXML
    private void onBackPressed(MouseEvent evt){
        stopCamera = true;
        
        if(webcam!=null){
            if(webcam.isOpen()){
                webcam.close();
            }
        }
        if(pictureIsTaken==true){
            if(isSaved==false){
                int i = Dialog.confirmMessage("Picture has not been saved. Do you want to save it?",
                        "Member's Information");
                if(i==1){
                    savePicture();
                    loadMemberList();
                }else if(i==0){
                    loadMemberList();
                }
            }else{
                loadMemberList();
            }
        }else{
            loadMemberList();
        }
    }
    
    @FXML
    private void onPressed(MouseEvent evt){
        Stage stage = (Stage)topBar1.getScene().getWindow();
        mouseX = evt.getSceneX();
        mouseY = evt.getSceneY();
    }
    
    public void setBranchName(String bName){
        this.strBranchName = bName;
        branchName.setText(bName + " Branch");
    }
    
    public void setCIFNo(String ID){
        this.CIF = ID;
        lblCIF.setText(ID);
    }
    
    public void setName(String Name){
        this.Name = Name;
        lblName.setText(Name);
    }
    
    public void setPicToImageView(){
        if(CIF.length()==9){
            //Check if member have picture
            if(newM.havePic(CIF)){
                btnStartCam.setText("Update Picture");
                String picPath = newM.getPicPath(CIF);
                try{
                    Image asd = new Image("file:" + picPath);
                    img.setImage(asd);
                }catch(Exception e){
                    Dialog.errorMessage(e.toString(), "Member's Information");
                    e.printStackTrace();
                }
            }else{
                img.setImage(null);
            }
        }else{
            //Check if member have picture
            if(dataMember.havePic(CIF)){
                btnStartCam.setText("Update Picture");
                String picPath = dataMember.getPicPath(CIF);
                try{
                    Image asd = new Image("file:" + picPath);
                    img.setImage(asd);
                }catch(Exception e){
                    Dialog.errorMessage(e.toString(), "Member's Information");
                    e.printStackTrace();
                }
            }else{
                img.setImage(null);
            }
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Rectangle clip = new Rectangle(675,310);
        clip.setArcHeight(40);
        clip.setArcWidth(40);
        bgImg.setClip(clip);
        dataMember = new MemberDataAccessor();
        newM = new NewMemberDataAccessor();
    }
    
}