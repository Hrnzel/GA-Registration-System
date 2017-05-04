package SQL;

import Dialog.Dialog;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CounterAccessor {
    
    private final String host = "jdbc:mysql://10.3.6.185:3306/lkbp";
    private final String user = "root";
    private final String pass = "1528";
    private Connection con;
    private ResultSet rs;
    private Statement st;
    private PreparedStatement ps;
    
    public String getCounter(String username){
        
        String count = "";
        String sql = "select * from terminal_counter where user_id like '"+username+"'";
        
        try{
            con = DriverManager.getConnection(host, user, pass);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                count = rs.getString("counter");
            }
        }catch(Exception e){
            Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
        }finally{
            try{
                con.close();
            }catch(Exception e){
                Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
            }
        }
        
        return count;
    }
    
    
    
    
}
