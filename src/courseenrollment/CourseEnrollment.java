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
   
        //queries
        String q0 = "SELECT * FROM Student";
        printStudentList("", q0, jdbc);
        
        String c1 = "MATH10000";
        String q1 = "SELECT firstName, lastName FROM Student "
                + "INNER JOIN CourseStudent ON student.id = CourseStudent.studentId "
                + "WHERE CourseStudent.courseId = " + "'" + c1 + "'";
        printStudentList(c1, q1, jdbc);
        
        String c2 = "PROG30000";
        String q2 =  "SELECT firstName, lastName FROM Student "
                + "INNER JOIN CourseStudent ON student.id = CourseStudent.studentId "
                + "WHERE CourseStudent.courseId = " + "'" + c2 + "'";
        printStudentList(c2, q2, jdbc);
        
        //disconnect from the database
        jdbc.disconnect();
    }
    
    //prints a list, obtained from the querym 
    private static void printStudentList(String course, String query, JdbcHelper jdbc){
        String s = "";
        if (course!=""){
            s = " in " + course;
        }
        try{
            ResultSet result = jdbc.query(query);
            System.out.println("Students" + s);
            System.out.println("==================");
            String id = "";
            while(result.next()){
                //get 3 field fromeach row
                if (s=="") {id = result.getString("id");}
                String first = result.getString("firstName"); 
                String last = result.getString("lastName");
                
                //print
                if (s=="") {System.out.printf("%9s:", id);}
                System.out.printf(" %s %s\n", first, last);
            }
            System.out.println("==================");
            System.out.println("\n");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}
