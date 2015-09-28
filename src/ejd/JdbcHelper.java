//JdbcHelper.java
//===============
//simple light JDBC Helper class for simplified interaction with a database
//
//AUTHOR:  Ryan West (Galina Galimova) (galiya991@gmail.com)
//CREATED: 18.09.2015
//UPDATED: 27.09.2015

package ejd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;


public class JdbcHelper {

    //instance member variables
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    String errorMessage;
    private boolean connected = false;
    
    //constructor
    
    public boolean isConnected(){return connected;}
    
    
    //connect to database with 3 params: URL, username, password
    //returns true if success, otherwise returns false
    //handles 3 types of exceptions: classNotFound, SQLException, Exception 
    public boolean connect(String url, String user, String pass){
        //inicialize variable as a false to begin with
        boolean connected = false;
        
        try{
            //generic purpose but can use only mysql
            //make db connecion using 3 param it will load driver automatically
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);
            //create sql statement object
            statement = connection.createStatement();
            
            connected = true;
        }
        catch(ClassNotFoundException e){
            errorMessage = "[ERROR]" + e.getMessage();
            System.err.println(errorMessage);
        }
        catch(SQLException e){
            errorMessage = "[ERROR]" + e.getSQLState() + e.getMessage(); 
            System.err.println(errorMessage);
        }
        catch(Exception e){
            errorMessage = "[ERROR]" + e.getMessage();
            System.err.println(errorMessage);
        }
           
        return connected;
    }
    
    //disconnects from the database
    public void disconnect(){
        try{resultSet.close();}catch(Exception e){}
        try{statement.close();} catch(Exception e){}
        try{connection.close();} catch(Exception e){}
    }
    
    //executes a sql query
    public ResultSet query(String sql){
        //reset
        errorMessage = "";
        resultSet = null;
        
        try{
            resultSet = statement.executeQuery(sql);
        }
        catch(SQLException e){
           errorMessage = "[ERROR]" + e.getSQLState() + e.getMessage();
           System.err.println(errorMessage);
        }
        catch(Exception e){
            errorMessage = "[ERROR]" + e.getMessage();
            System.err.println(errorMessage);
        }
        return this.resultSet;
    }
    
    //executes a sql statement and returns zero or # of rows updated 
    //if failed, returns -1
    public int update(String sql){
        int result = -1; //default return value
        errorMessage = ""; 
        
        try{
            result = statement.executeUpdate(sql);
        }
        catch(SQLException e){
            errorMessage = "[ERROR] "  + e.getSQLState() + ": " + e.getMessage();
            System.err.println(errorMessage);
        }
        catch(Exception e){
            errorMessage = "[ERROR]" + e.getMessage();
            System.err.println(errorMessage);
        }
        return result;
    }
    
    //returns the current error message
    public String getErrorMessage(){
        return errorMessage;
    }
}
