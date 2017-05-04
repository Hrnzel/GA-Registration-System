package Controller;

import Dialog.Dialog;
import SQL.CounterAccessor;
import SQL.MemberDataAccessor;
import com.github.sarxos.webcam.Webcam;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class RegistrationMenuController implements Initializable {

    CounterAccessor count = new CounterAccessor();
    MemberDataAccessor member = new MemberDataAccessor();
    
    RegistrationFormController regForm;
    RegisteredMembersController regMem;
    AddUserController addUser;
    
    Stage secStage = new Stage();
    Stage addStage = new Stage();
    
    @FXML private Menu account;
    
    @FXML private Label exit;
    @FXML private Pane topBar;
    @FXML private Pane body;
    double mouseX, mouseY;
    private Webcam webcam;
    private String username;
    
    public void setUsername(String username){
        this.username = username;
    }

    public Webcam getWebcam() {
        return webcam;
    }

    public void setWebcam(Webcam webcam) {
        this.webcam = webcam;
    }
    
    @FXML private Button cabanas,marilao,cruz,apalit,
            dinalupihan, main, balanga, baliuag, starosa,
            sanmiguel,gapan;
    @FXML private Label cabanas1,marilao1,cruz1,apalit1,
            dinalupihan1, main1, balanga1, baliuag1, starosa1,
            sanmiguel1,gapan1;
    
    private int ctr=0;
    
    Timeline timeline;
    
    @FXML private Label lblCounter;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblCounter.setText("0");
        updateCounter();
    }
    
    public void readCounter(){
        String counter = count.getCounter(username);
        lblCounter.setText(counter);
    }
    
    public void updateCounter(){
        timeline = new Timeline(
            new KeyFrame(Duration.seconds(0), (ActionEvent actionEvent) -> {
                readCounter();
        }),
              new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    public void checkUser(){
        if(!username.equalsIgnoreCase("admin")){
            account.setVisible(false);
        }
    }
    
    public void about(ActionEvent e){
        try {
            Stage stage = (Stage)body.getScene().getWindow();
            
            Stage secondStage = new Stage();
            secondStage.initStyle(StageStyle.UNDECORATED);
            secondStage.initOwner(stage);
            secondStage.initModality(Modality.WINDOW_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AboutGAReg.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            secondStage.setScene(scene);
            
            secondStage.show();
        } catch (IOException ex) {
            Dialog.errorMessage(ex.toString(),"Main Menu","Error loading FXML");
        }
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
            Dialog.errorMessage(e.toString(), "Registration System", "Error loading fxml.");
        }
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        login = loader.getController();
        
        login.setSceneOnCenter();
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
    
    public void setSceneOnCenter(){
        Stage stage = (Stage)topBar.getScene().getWindow();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
    
    public void switchToPhotoApp(){
        Main_MenuController menu;
        Stage stage = (Stage)body.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Main_Menu.fxml"));
        Parent root = null;
        try{
            root = loader.load();
        }catch(Exception e){
            Dialog.errorMessage(e.toString(), "Registration System", "Error loading fxml.");
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        menu = loader.getController();

        menu.setUsername(this.username);
        menu.setSceneOnCenter();
        if(webcam==null){
            menu.selectWebCam();
        }else{
            menu.setWebcam(getWebcam());
        }
        
    }
    
    public void handleButtonAction(ActionEvent e){
        
        Stage stage = (Stage)cabanas.getScene().getWindow();
        FXMLLoader loader = null;
        String name = "";
        
        if(e.getSource()==cabanas){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Cabanas";
        }
        else if(e.getSource()==main){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Main";
        }
        else if(e.getSource()==marilao){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Marilao";
        }
        else if(e.getSource()==apalit){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Apalit";
        }
        else if(e.getSource()==cruz){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Cruz";
        }
        else if(e.getSource()==gapan){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Gapan";
        }
        else if(e.getSource()==dinalupihan){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Dinalupihan";
        }
        else if(e.getSource()==balanga){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Balanga";
        }
        else if(e.getSource()==baliuag){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Baliuag";
        }
        else if(e.getSource()==starosa){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Sta Rosa";
        }
        else if(e.getSource()==sanmiguel){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "San Miguel";
        }
        
        Parent root = null;
        try{
            root = loader.load();
        }catch(Exception ex){
            Dialog.errorMessage(ex.toString(), "Main Menu", "Error loading fxml.");
        }

        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        
        regForm = loader.getController();
        
        regForm.setUsername(this.username);
        regForm.setUserData();
        regForm.setBranchName(name);
        regForm.setDataToTable();
        regForm.setInitialInfo();
        regForm.setSceneOnCenter();
    }
    
    public void selectBranch(MouseEvent e){
        Stage stage = (Stage)cabanas.getScene().getWindow();
        FXMLLoader loader = null;
        String name = "";
        
        if(e.getSource()==cabanas1){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Cabanas";
        }
        else if(e.getSource()==main1){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Main";
        }
        else if(e.getSource()==marilao1){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Marilao";
        }
        else if(e.getSource()==apalit1){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Apalit";
        }
        else if(e.getSource()==cruz1){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Cruz";
        }
        else if(e.getSource()==gapan1){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Gapan";
        }
        else if(e.getSource()==dinalupihan1){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Dinalupihan";
        }
        else if(e.getSource()==balanga1){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Balanga";
        }
        else if(e.getSource()==baliuag1){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Baliuag";
        }
        else if(e.getSource()==starosa1){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "Sta Rosa";
        }
        else if(e.getSource()==sanmiguel1){
            loader = new FXMLLoader(getClass().getResource("/View/Registration Form.fxml"));
            name = "San Miguel";
        }
        
        Parent root = null;
        try{
            root = loader.load();
        }catch(Exception ex){
            Dialog.errorMessage(ex.toString(), "Main Menu", "Error loading fxml.");
        }

        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        
        regForm = loader.getController();
        
        regForm.setUsername(username);
        regForm.setUserData();
        regForm.setBranchName(name);
        regForm.setSceneOnCenter();
        regForm.setDataToTable();
        regForm.setInitialInfo();
    }
    
    public void close(ActionEvent e){
        Platform.exit();
    }
    
    public void goToViewRegMem(ActionEvent e){
        
        if(secStage.getScene()==null){
            secStage.initStyle(StageStyle.UNDECORATED);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Registered Members.fxml"));
        Parent root = null;
        try{
            root = loader.load();
        }catch(Exception ex){
            Dialog.errorMessage(ex.toString(), "Main Menu", "Error loading fxml.");
        }

        regMem = loader.getController();

        Scene scene = new Scene(root);

        secStage.setScene(scene);
        if(secStage.isShowing()){
            secStage.requestFocus();
        }else{
            secStage.show();
        }
        regMem.setUsername(username);
        regMem.setSceneOnCenter();
    }
    
    public void goToAddUser(ActionEvent e){
        if(addStage.getScene()==null){
            addStage.initStyle(StageStyle.UNDECORATED);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Add User.fxml"));
        Parent root = null;
        try{
            root = loader.load();
        }catch(Exception ex){
            Dialog.errorMessage(ex.toString(), "Main Menu", "Error loading fxml.");
        }

        addUser = loader.getController();

        Scene scene = new Scene(root);

        addStage.setScene(scene);
        if(addStage.isShowing()){
            addStage.requestFocus();
        }else{
            addStage.show();
        }
        addUser.setUsername(username);
        addUser.setSceneOnCenter();
        addUser.requestFocus();
    }
    
    public void importRM(ActionEvent evt){
        importToExcelRM();
    }
    
    public void importToExcelRM(){
        XSSFWorkbook workbook = new XSSFWorkbook(); 
        
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Registered Members");
        
        //This data needs to be written (Object[])
        Map<String, Object[]> data = member.getRegisteredMembers();
        
        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        
        for (String key : keyset){
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
                Cell cell = row.createCell(cellnum++);
                if(rownum==1){
                    cell.setCellStyle(style);
                    cell.setCellValue((String)obj);
                }else
                    cell.setCellValue((String)obj);
            }
        }
        try{
            String path="";
            Stage stage = (Stage)cabanas.getScene().getWindow();
            
            FileChooser fileChoose = new FileChooser();
            fileChoose.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Workbook (.xlsx)", "*.xlsx")
            );
            File file = fileChoose.showSaveDialog(stage);
            if(file!=null){
                path = file.getAbsolutePath();
                //Write the workbook in file system
                FileOutputStream out = new FileOutputStream(new File(path));
                workbook.write(out);                
                out.close();
                
                Dialog.infoMessage("Excel file successfully imported!", "Registration System");
            }
        } 
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void importTermCount(ActionEvent evt){
        importToExcelTermCount();
    }
    
    public void importToExcelTermCount(){
        XSSFWorkbook workbook = new XSSFWorkbook(); 
        
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Registered Members");
        
        //This data needs to be written (Object[])
        Map<String, Object[]> data = member.getRegisteredCountByUser();
        
        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        
        for (String key : keyset){
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
                Cell cell = row.createCell(cellnum++);
                if(rownum==1){
                    cell.setCellStyle(style);
                    cell.setCellValue((String)obj);
                }else
                    cell.setCellValue((String)obj);
            }
        }
        try{
            String path="";
            Stage stage = (Stage)cabanas.getScene().getWindow();
            
            FileChooser fileChoose = new FileChooser();
            fileChoose.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Workbook (.xlsx)", "*.xlsx")
            );
            File file = fileChoose.showSaveDialog(stage);
            if(file!=null){
                path = file.getAbsolutePath();
                //Write the workbook in file system
                FileOutputStream out = new FileOutputStream(new File(path));
                workbook.write(out);                
                out.close();
                
                Dialog.infoMessage("Excel file successfully imported!", "Registration System");
            }
        } 
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void importByBranchCount(ActionEvent evt){
        importToExcelByBranchCount();
    }
    
    public void importToExcelByBranchCount(){
        XSSFWorkbook workbook = new XSSFWorkbook(); 
        
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Registered Members");
        
        //This data needs to be written (Object[])
        Map<String, Object[]> data = member.getRegisteredCountByBranch();
        
        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        
        for (String key : keyset){
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
                Cell cell = row.createCell(cellnum++);
                if(rownum==1){
                    cell.setCellStyle(style);
                    cell.setCellValue((String)obj);
                }else
                    cell.setCellValue((String)obj);
            }
        }
        try{
            String path="";
            Stage stage = (Stage)cabanas.getScene().getWindow();
            
            FileChooser fileChoose = new FileChooser();
            fileChoose.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Workbook (.xlsx)", "*.xlsx")
            );
            File file = fileChoose.showSaveDialog(stage);
            if(file!=null){
                path = file.getAbsolutePath();
                //Write the workbook in file system
                FileOutputStream out = new FileOutputStream(new File(path));
                workbook.write(out);                
                out.close();
                
                Dialog.infoMessage("Excel file successfully imported!", "Registration System");
            }
        } 
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void importAll(ActionEvent evt){
        importToExcelAll();
    }
    
    public void importToExcelAll(){
        XSSFWorkbook workbook = new XSSFWorkbook(); 
        
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Members");
        
        //This data needs to be written (Object[])
        Map<String, Object[]> data = member.getAllMembers();
        
        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        
        for (String key : keyset){
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
                Cell cell = row.createCell(cellnum++);
                if(rownum==1){
                    cell.setCellStyle(style);
                    cell.setCellValue((String)obj);
                }else
                    cell.setCellValue((String)obj);
            }
        }
        try{
            String path="";
            Stage stage = (Stage)cabanas.getScene().getWindow();
            
            FileChooser fileChoose = new FileChooser();
            fileChoose.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Workbook (.xlsx)", "*.xlsx")
            );
            File file = fileChoose.showSaveDialog(stage);
            if(file!=null){
                path = file.getAbsolutePath();
                //Write the workbook in file system
                FileOutputStream out = new FileOutputStream(new File(path));
                workbook.write(out);                
                out.close();
                
                Dialog.infoMessage("Excel file successfully imported!", "Registration System");
            }
        } 
        catch (Exception e){
            e.printStackTrace();
        }
    }
}