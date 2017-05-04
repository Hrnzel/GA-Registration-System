package SQL;

import Dialog.Dialog;
import Model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDataAccessor {
    
    private final String host = "jdbc:mysql://10.3.6.185:3306/lkbp";
    private final String user = "root";
    private final String pass = "1528";
    private Connection con;
    private ResultSet rs;
    private Statement st;
    private PreparedStatement ps;
    
    public List<User> getAccountFromDB(String username){
        String sql = "select * from user where username like '"+username+"'";
        List<User> userList = new ArrayList<>();
        
        try{
            con = DriverManager.getConnection(host, user, pass);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    String id = rs.getString(1);
                    String user = rs.getString(2);
                    String pass = rs.getString(3);
                    String first = rs.getString(4);
                    String middle = rs.getString(5);
                    String last = rs.getString(6);
                    User userr = new User(id,user,pass,first,middle,last);
                    userList.add(userr);
                }
            }else{
                Dialog.errorMessage("Account doesn't exist!","Login");
            }
        }catch(Exception e){
            Dialog.errorMessage(e.toString(), "Login", "There was an SQL error:");
        }finally{
            try{
                con.close();
            }catch(Exception e){
                Dialog.errorMessage(e.toString(), "Login", "There was an SQL error:");
            }
        }
        
        return userList;
    }
    
    
    
}
