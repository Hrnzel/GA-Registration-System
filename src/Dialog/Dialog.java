package Dialog;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class Dialog {
    
    public static void infoMessage(String message, String title, String header){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
    
    public static void infoMessage(String message, String title){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    
    public static void errorMessage(String message, String title, String header){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
    
    public static void errorMessage(String message, String title){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    
    public static void warningMessage(String message, String title, String header){
        Alert alert = new Alert(AlertType.WARNING);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
    
    public static void warningMessage(String message, String title){
        Alert alert = new Alert(AlertType.WARNING);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    
    public static int confirmMessage(String message, String title, String header){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(header);
        
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == yesButton){
            return 1;
        }else if(result.get() == noButton){
            return 0;
        }else{
            return -1;
        }
    }
    
    public static int confirmMessage(String message, String title){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == yesButton){
            return 1;
        }else if(result.get() == noButton){
            return 0;
        }else{
            return -1;
        }
    }
}