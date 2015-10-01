//JdbcHelper.java
//===============
//simple light JDBC Helper class for simplified interaction with a database
//
//AUTHOR:  Ryan West (Galina Galimova) (galiya991@gmail.com)
//CREATED: 18.09.2015
//UPDATED: 30.09.2015

package ejd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;


public class JdbcHelper {

    //instance member variables
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    String errorMessage;
    private boolean connected = false;
    //for prepared statement
    private PreparedStatement activeStatement;
    private String activeSql;
    
    public boolean isConnected(){return connected;}
    
    public JdbcHelper(){
        connection = null;
        statement = null;
        resultSet = null;
        activeSql = "";
        activeStatement = null;
        errorMessage = "";
        
    }
    
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
    
    //executes a sql query
    public ResultSet query(String sql,  ArrayList<Object> params){
        //reset
        errorMessage = "";
        resultSet = null;
        
        try{
            //create prerared statement only if it doesn`t exist
            if(!activeSql.equals(sql)){
                activeStatement = connection.prepareStatement(sql);
                activeSql = sql; //update 
            }
            //set parameters for prepared statement (down-cast from object to specific datatype)
            if (params!=null){
                setParametersForPreparedStatement(params);
            }
            //execute prerarred statement
            resultSet = activeStatement.executeQuery();
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
    
    //execuding three steps whithing one method
    public int update(String sql, ArrayList<Object> params) {

        int result = -1; //default return value
        errorMessage = "";

        try {
            //create prerared statement only if it doesn`t exist
            if (!activeSql.equals(sql)) {
                activeStatement = connection.prepareStatement(sql);
                activeSql = sql; //update 
            }

            //set parameters for prepared statement (down-cast from object to specific datatype)
            if (params != null) {
                setParametersForPreparedStatement(params);
            }
            //execute prerarred statement
            result = activeStatement.executeUpdate();
        } catch (SQLException e) {
            errorMessage = "[ERROR] " + e.getSQLState() + ": " + e.getMessage();
            System.err.println(errorMessage);
        } catch (Exception e) {
            errorMessage = "[ERROR] " + e.getMessage();
            System.err.println(errorMessage);
        }
        //if it is success, it will return prorer value, othervise will be exception throun
        return result;
    }
    
    //down-cast all params to original type then call setXXX()
    private void setParametersForPreparedStatement(ArrayList<Object> params){
        errorMessage = "";
        Object param = null;
        try{
            for(int i = 1; i <= params.size(); i++){
                param = params.get(i - 1);
                if(param instanceof Integer)
                    activeStatement.setInt(i, (int)param);
                else if(param  instanceof Double)
                    activeStatement.setDouble(i, (double)param);
                else if(param  instanceof String)
                    activeStatement.setString(i, (String)param);
            }
        }
        catch(SQLException e){
            errorMessage = "[ERROR] "  + e.getSQLState() + ": " + e.getMessage();
            System.err.println(errorMessage);
        }
        catch(Exception e){
             errorMessage = "[ERROR] " + e.getMessage();
             System.err.println(errorMessage);
        }
    }
    //returns the current error message
    public String getErrorMessage(){
        return errorMessage;
    }
    
}
