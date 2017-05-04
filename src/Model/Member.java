package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class Member {
    private final StringProperty ID = new SimpleStringProperty(this, "ID");
    public final StringProperty IDProperty(){
        return ID;
    }
    private final StringProperty Name = new SimpleStringProperty(this, "Name");
    public final StringProperty firstNameProperty(){
        return Name;
    }
    private final StringProperty Status = new SimpleStringProperty(this, "Status");
    public final StringProperty statusProperty(){
        return Status;
    }
    
    public final void setStatus(String status){
        this.Status.set(status);
    }
    
    public String getStatus(){
        return this.Status.get();
    }
    
    public final void setID(String id){
        this.ID.set(id);
    }
    
    public String getID(){
        return this.ID.get();
    }
    
    public void setName(String name){
        this.Name.set(name);
    }
    
    public String getName(){
        return this.Name.get();
    }
    
    public Member() {}
    
    public Member(String id, String Name, String status){
        setID(id);
        setName(Name);
        setStatus(status);
    }
}
