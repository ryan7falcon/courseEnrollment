/*
 * CourseEnrollment.java
 *----------------------
 * Accesses the database and prints a list of all students and students enrolled in certain courses
 */
package courseenrollment;

import ejd.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;

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
        
        //connection: location, user name, password
        jdbc.connect("jdbc:mysql://localhost:3306/ejd", 
                                        "root", "dataPASS");
   
        //query to get a list of all students
        String q0 = "SELECT * FROM Student";
        //getting the result and printing 
        printStudentList("Students", true, q0, null, jdbc); 
        
        //getting and printing the list of students in a particular course
        printStudentsInCourse("MATH10000", jdbc);
        printStudentsInCourse("PROG30000", jdbc);
        
        //disconnect from the database
        jdbc.disconnect();
    }
    
    //gets and print a list of students using a given query
    //params: a header of the table, boolean deisplayId - display id of a student or not,
    //a query, instance of JdbcHelper
    private static void printStudentList(String header, boolean displayId, String query, ArrayList<Object> params, JdbcHelper jdbc){
        
        try{
            ResultSet result;
            //get a list
            if (params!=null){ //if this is a list of students for a certain course
                result = jdbc.query(query, params);}
            else{ //if this is a list of all students
                result = jdbc.query(query);
            }
                
            //header
            System.out.println(header);
            System.out.println("==================");
           
            String id = "";
            while(result.next()){
                //id is printed only if displayId is true
                if (displayId) {id = result.getString("id");}
                //getting first name and last name
                String first = result.getString("firstName"); 
                String last = result.getString("lastName");
                //printing the result
                if (displayId) {System.out.printf("%9s:", id);}
                System.out.printf(" %s %s\n", first, last);
            }
            //formatting
            System.out.println("==================");
            System.out.println("\n");
        }
        //handling exceptions
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    
    //gets and prints a list of students in a given course
    //params: courseId and instance of JdbcHelper
    private static void printStudentsInCourse(String course, JdbcHelper jdbc){
        String q = "SELECT firstName, lastName FROM Student "
                + "INNER JOIN CourseStudent ON student.id = CourseStudent.studentId "
                + "WHERE CourseStudent.courseId = ?";
        //parameter for prepareStatement
         ArrayList<Object> params = new ArrayList<Object>();
         params.add(course);
        printStudentList("Students in " + course, false, q, params, jdbc); 
    }
}
