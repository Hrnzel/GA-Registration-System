package SQL;

import Dialog.Dialog;
import Model.NewMember;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NewMemberDataAccessor {
    
    private final String host = "jdbc:mysql://10.3.6.185:3306/lkbp";
    private final String user = "root";
    private final String pass = "1528";
    private Connection con;
    private ResultSet rs;
    private Statement st;
    private PreparedStatement ps;
    
    public List<NewMember> getNewMemberList(String sql){
        List<NewMember> list = new ArrayList<>();
        
        try{
            con = DriverManager.getConnection(host, user, pass);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    String account = rs.getString("account_no");
                    String name = rs.getString("name");
                    NewMember newM = new NewMember(account,name);
                    list.add(newM);
                }
            }
        }catch(Exception e){
            Dialog.errorMessage(e.toString(), "New Member","There is an SQL Error");
        }finally{
            try{
                con.close();
            }catch(Exception e){
                Dialog.errorMessage(e.toString(), "New Member","There is an SQL Error");
            }
        }
        return list;
    }
    
    public boolean addMember(String account, String name, String branchName){
        boolean success = false;
        String sql = "";
        sql = "insert into tbl_new_member(`account_no`,`name`,`branch`)"
                + " values('"+account+"','"+name+"','"+branchName+"')";
        try{
            con = DriverManager.getConnection(host, user, pass);
            st = con.createStatement();
            int i = st.executeUpdate(sql);
            if(i!=0){
                success = true;
            }
        }catch(Exception e){
            Dialog.errorMessage(e.toString(), "New Member","There is an SQL Error");
        }finally{
            try{
                con.close();
            }catch(Exception e){
                Dialog.errorMessage(e.toString(), "New Member","There is an SQL Error");
            }
        }
        
        
        return success;
    }
    
    
    public boolean havePic(String CIF){
        String sql = "select pic_path from tbl_new_member where account_no like '"+CIF+"'";
        boolean have = false;
        try{
            con = DriverManager.getConnection(host,user,pass);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                String path = rs.getString(1);
                have = (path!=null&&!path.isEmpty());
            }
        }catch(SQLException e){
            Dialog.errorMessage(e.toString(), "Error", "There is an SQL Error");
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
            }
        }
        return have;
    }
    
    public String getPicPath(String CIF){
        String sql = "select pic_path from tbl_new_member where account_no like '"+CIF+"'";
        String have = "";
        try{
            con = DriverManager.getConnection(host,user,pass);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.first()){
                have = rs.getString(1);
            }else{
                Dialog.errorMessage("Member doesn't exists! Please input a valid Account No.", "Error");
            }
        }catch(SQLException e){
            Dialog.errorMessage(e.toString(), "Error", "There is an SQL Error");
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
            }
        }
        return have;
    }
    
    public boolean updateDBPicPath(String CIF, String picPath){
        picPath = picPath.replace("\\","\\\\");
        String sql = "UPDATE tbl_new_member SET pic_path = '"+picPath+"'"
                + " where Account_no = '"+CIF+"'";
        boolean succ = false;
        try{
            con = DriverManager.getConnection(host,user,pass);
            st = con.createStatement();
            int i = st.executeUpdate(sql);
            succ = (i!=0);
        }catch(SQLException e){
            Dialog.errorMessage(e.toString(), "Error","There is an SQL Error");
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
            }
        }
        return succ;
    }
    
}
