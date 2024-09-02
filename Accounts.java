import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection conn;
    private Scanner sc;

    public Accounts(Connection conn, Scanner sc){
        this.conn = conn;
        this.sc = sc;
    }

    public long open_account(String email){
        if(!account_exist(email)){
            String query = "INSERT INTO accounts (acc_num, full_name, email, balance, security_pin)VALUES(?,?,?,?,?)";

            System.out.println("Enter Full Name: ");
            String name = sc.next();
            System.out.println("Enter balance: ");
            double balance = sc.nextDouble();
            System.out.println("Enter Security Pin: ");
            String pin = sc.next();

            try {
                long account_num = generateAccountNumber();
                PreparedStatement pstm = conn.prepareStatement(query);
                pstm.setLong(1,account_num);
                pstm.setString(2,name);
                pstm.setString(3,email);
                pstm.setDouble(4,balance);
                pstm.setString(5,pin);
                int row = pstm.executeUpdate();
                if(row > 0){
                   return account_num;
                }else {
                    throw new RuntimeException("Account creation Failed");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        else {
            System.out.println("Account already exist");
        }
        throw new RuntimeException("Account creation Failed");
    }


    public long getAccount_number(String email){
        String query = "SELECT acc_num FROM Accounts WHERE email = ?";
        try {
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.setString(1,email);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()){
                return rs.getLong("acc_num");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account Number doesn't exist");
    }

    public long generateAccountNumber(){
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT acc_num FROM accounts ORDER BY acc_num DESC LIMIT 1");
            if (rs.next()){
                long last_acc_number = rs.getLong("acc_num");
                return last_acc_number+1;
            }
            else {
                return 10000100;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 10000100;
    }


    public boolean account_exist(String email) {
        String query = "SELECT * FROM accounts WHERE email = ?";
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
