package db2013136046;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class MyConnection {
    public static Connection con = null;
    public static Connection makeCon(){
        
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Driver not Found...");
        }
        // Establish a connection jdbc : protocol
        String url = "jdbc:sqlserver://DESKTOP-B8RQSGC\\SQLEXPRESS01:1433;databaseName=Mydatabase";
        try{
            con=DriverManager.getConnection(url,"joo","1234");
            JOptionPane.showMessageDialog(null,"Connection is Established...");
            return con;
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Connection is not Established...");
            return null;
        }
    }
    
}
