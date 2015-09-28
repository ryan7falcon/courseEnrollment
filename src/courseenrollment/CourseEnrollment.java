/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseenrollment;

import ejd.JdbcHelper;
import java.sql.ResultSet;

/**
 *
 * @author Ryan West (Galina Galimova) (galiya991@gmail.com)
 */
public class CourseEnrollment {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       //create instance of JDBC helper
        JdbcHelper jdbc = new JdbcHelper();
        
        //test connection: location, user name, password
        boolean connected = jdbc.connect("jdbc:mysql://localhost:3306/ejd", 
                                        "root", "dataPASS");
        System.out.println(connected);
        //clean-up, close method
        String sql = "SELECT * FROM Book";
        
        try{
            ResultSet result = jdbc.query(sql);
            while(result.next()){
                //get 3 field fromeach row
                int id = result.getInt("id"); //or can be result.getInt(1);
                String isbn = result.getString("isbn"); //or can be result.getString(ISBN); 
                String title = result.getString("title");
                
                //print
                System.out.printf("%1d %7s %s\n", id, isbn, title);//int, 2 strings 
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        jdbc.disconnect();
    }
    
}
