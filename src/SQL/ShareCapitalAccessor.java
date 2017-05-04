package SQL;

import Dialog.Dialog;
import Model.ShareCapital;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ShareCapitalAccessor {
    private final String host = "jdbc:mysql://10.3.6.185:3306/lkbp";
    private final String user = "root";
    private final String pass = "1528";
    private Connection con;
    private ResultSet rs;
    private Statement st;
    private PreparedStatement ps;
    
    public List<ShareCapital> getShareCapital(String CIF){
        String sql = "select CIFKey, common_share, preferred_share, fixed_deposit, "
                + "total, loan_status from share_capital where CIFKey like '"+CIF+"'";
        List<ShareCapital> share = new ArrayList<>();
        try{
            con = DriverManager.getConnection(host,user,pass);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                String id = rs.getString(1);
                String comShare = rs.getString(2);
                String prefShare = rs.getString(3);
                String fixDep = rs.getString(4);
                String total = rs.getString(5);
                String status = rs.getString(6);
                share.add(new ShareCapital(id,comShare,prefShare,fixDep,total,status));
            }
        }catch(Exception e){
            Dialog.errorMessage(e.toString(),"Registration System","There was an SQL Error:");
        }finally{
            try{
                con.close();
            }catch(Exception e){
                Dialog.errorMessage(e.toString(),"Registration System","There was an SQL Error:");
            }
        }
        
        return share;
    }
    
}
