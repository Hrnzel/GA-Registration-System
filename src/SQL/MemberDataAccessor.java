package SQL;

import Dialog.Dialog;
import Model.Member;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MemberDataAccessor {
   
    private final String host = "jdbc:mysql://10.3.6.185:3306/lkbp";
    private final String user = "root";
    private final String pass = "1528";
    private Connection con;
    private ResultSet rs;
    private Statement st;
    private PreparedStatement ps;
    
    CounterAccessor count = new CounterAccessor();
    
    public ObservableList<Member> getMembersList(String sql){
        ObservableList<Member> memberList = FXCollections.observableArrayList();
        try{
            con = DriverManager.getConnection(host,user,pass);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                String cif = rs.getString("account_no");
                String Name = rs.getString("name");
                String status = rs.getString("Status");
                Member member = new Member(cif, Name, status);
                memberList.add(member);
                
            }
        }catch(SQLException e){
            Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
            }
        }
        return memberList;
    }
    
    public boolean havePic(String CIF){
        String sql = "select pic_path from member where account_no like '"+CIF+"'";
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
        String sql = "select pic_path from member where account_no like '"+CIF+"'";
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
        String sql = "UPDATE member SET pic_path = '"+picPath+"'"
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
    
    public boolean registerMember(String CIF, String username){
        boolean success = false;
        String counter = count.getCounter(username);
        int counters = Integer.parseInt(counter) + 1;
        
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new Timestamp(System.currentTimeMillis()));
        
        String sql = "update member set status = 'REGISTERED', user_id = '"+username+"', "
                + "date_registered = '"+timeStamp+"' where account_no like '"+CIF+"'";
        String sql2 = "update terminal_counter set counter = "+counters+" where user_id like '"+username+"'";
        
        try{
           con = DriverManager.getConnection(host, user, pass);
           st = con.createStatement();
           int i = st.executeUpdate(sql);
           int j = st.executeUpdate(sql2);
           success = (i!=0 && j!=0);
        }catch(Exception e){
            Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
            }
        }
        return success;
    }
    
    public String getRegisteredCountByBranch(String branchName){
        String count="";
        String sql = "select count(*) from member where status like 'Registered' and branch like '"+branchName+"'";
        try{
            con = DriverManager.getConnection(host, user, pass);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                count = rs.getString(1);
            }
            
        }catch(Exception e){
            Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
            }
        }
        return count;
    }
    
    public String getRegisteredMembersCount(){
        String count="";
        String sql = "select count(*) from member where status like 'Registered'";
        try{
            con = DriverManager.getConnection(host, user, pass);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                count = rs.getString(1);
            }
            
        }catch(Exception e){
            Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
            }
        }
        return count;
    }
    
    public Map<String, Object[]> getRegisteredMembers(){
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        
        String sql = "SELECT m.account_no, m.name, m.branch, "
                + "concat(s.first_name,' ',s.last_name) "
                + "FROM member m JOIN user s "
                + "ON m.user_id = s.user_id "
                + "WHERE m.status like 'Registered' "
                + "ORDER BY m.branch";
        try{
            con = DriverManager.getConnection(host, user, pass);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            int i=1;
            data.put(i+"", new Object[] {"Member Code","Name","Branch","Registered By"});
            while(rs.next()){
                String memcode = rs.getString(1);
                String name = rs.getString(2);
                String branch = rs.getString(3);
                String regBy = rs.getString(4);
                i++;
                data.put(i+"", new Object[] {memcode, name, branch, regBy});
            }
            
        }catch(Exception e){
            Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
            }
        }
        
        return data;
    }
    
    public Map<String, Object[]> getRegisteredCountByUser(){
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        
        String sql = "SELECT count(*), "
                + "concat(s.first_name,' ',s.last_name) "
                + "FROM member m JOIN user s "
                + "ON m.user_id = s.user_id "
                + "WHERE m.status like 'Registered' "
                + "GROUP BY 2";
        try{
            con = DriverManager.getConnection(host, user, pass);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            int i=1;
            data.put(i+"", new Object[] {"Terminal Counter","Terminal User"});
            while(rs.next()){
                String count = rs.getString(1);
                String name = rs.getString(2);
                i++;
                data.put(i+"", new Object[] {count, name});
            }
            
        }catch(Exception e){
            Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
            }
        }
        
        return data;
    }
    
    public Map<String, Object[]> getRegisteredCountByBranch(){
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        
        String sql = "SELECT count(*), "
                + "branch "
                + "FROM member "
                + "WHERE status like 'Registered' "
                + "GROUP BY 2 "
                + "ORDER BY branch";
        try{
            con = DriverManager.getConnection(host, user, pass);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            int i=1;
            data.put(i+"", new Object[] {"Registered Members' Count","Branch"});
            while(rs.next()){
                String count = rs.getString(1);
                String branch = rs.getString(2);
                i++;
                data.put(i+"", new Object[] {count, branch});
            }
            
        }catch(Exception e){
            Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
            }
        }
        
        return data;
    }
    
    public Map<String, Object[]> getAllMembers(){
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        
        String sql = "SELECT account_no, name, branch from member order by branch";
        try{
            con = DriverManager.getConnection(host, user, pass);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            int i=1;
            data.put(i+"", new Object[] {"Member Code","Name","Branch"});
            while(rs.next()){
                String code = rs.getString(1);
                String name = rs.getString(2);
                String branch = rs.getString(3);
                i++;
                data.put(i+"", new Object[] {code, name, branch});
            }
            
        }catch(Exception e){
            Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                Dialog.errorMessage(e.toString(),"Error", "There is an SQL Error:");
            }
        }
        
        return data;
    }
}