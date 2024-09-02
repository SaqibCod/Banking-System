import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Users {
    private Connection conn;
    private Scanner sc;

    public Users(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }


    //Registration
    public void register() {
        sc.nextLine();
        System.out.print("Enter Full Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        //check user already exist
        if(user_exist(email)){
            System.out.println("Already user exist");
            return;
        }
        String query = "INSERT INTO users VALUES(?,?,?)";
        try{
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.setString(1,name);
            pstm.setString(2,email);
            pstm.setString(3,password);
            int rowAffected = pstm.executeUpdate();
            if(rowAffected > 0){
                System.out.println("Insertion Succesfull: "+ rowAffected);
            }
            else {
                System.out.println("Insertion Failed...");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Login User
    public String login() {
        sc.nextLine();
        System.out.println("Enter your email: ");
        String email = sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.nextLine();

        String query = "SELECT * FROM users WHERE email=? AND password = ?";
        try{
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.setString(1,email);
            pstm.setString(2,password);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                return email;
            }
            else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Check User
    public boolean user_exist(String email)  {
        String query = "SELECT * FROM users WHERE email = ?";
        try {
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.setString(1,email);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()){
                return true;
            }
            else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
