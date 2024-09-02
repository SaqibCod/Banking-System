import java.sql.*;
import java.util.Scanner;

public class AccountManager {
    private Connection conn;
    private Scanner sc;

    public AccountManager(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public void debit_money(long acc_num){
        sc.nextLine();
        System.out.println("Enter Amount: ");
        double amount = sc.nextDouble();
        System.out.println("Enter Security Pin: ");
        String pin = sc.next();

        try{
            conn.setAutoCommit(false);
            if (acc_num != 0){
                PreparedStatement pstm = conn.prepareStatement("SELECT * FROM accounts WHERE acc_num = ? AND security_pin = ?");
                pstm.setLong(1,acc_num);
                pstm.setString(2,pin);
                ResultSet rs = pstm.executeQuery();
                if (rs.next()) {
                    double curr_bal = rs.getDouble("balance");
                    if(amount <= curr_bal){
                        String debit_query = "UPDATE Account SET balance = balance - ? WHERE acc_num = ?";
                        PreparedStatement pstm1 = conn.prepareStatement(debit_query);
                        pstm1.setDouble(1,amount);
                        pstm1.setLong(2,acc_num);
                        int rowAffected = pstm1.executeUpdate();
                        if (rowAffected > 0){
                            System.out.println("Rs. "+ amount+ "debited Successfully");
                            conn.commit();
                            conn.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            conn.rollback();
                            conn.setAutoCommit(true);
                        }

                    }else {
                        System.out.println("Not sufficient amount");

                    }

                }else {
                    System.out.println("Invalid Pin");
                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void credit_money(long acc_num){
        sc.nextLine();
        System.out.println("Enter Amount: ");
        double amount = sc.nextDouble();
        System.out.println("Enter Security Pin: ");
        String pin = sc.next();

        try{
            conn.setAutoCommit(false);
            if (acc_num != 0){
                PreparedStatement pstm = conn.prepareStatement("SELECT * FROM accounts WHERE acc_num = ? AND security_pin = ?");
                pstm.setLong(1,acc_num);
                pstm.setString(2,pin);
                ResultSet rs = pstm.executeQuery();
                if (rs.next()) {

                    String credit_query = "UPDATE Account SET balance = balance + ? WHERE acc_num = ?";
                    PreparedStatement pstm1 = conn.prepareStatement(credit_query);
                    pstm1.setDouble(1,amount);
                    pstm1.setLong(2,acc_num);
                    int rowAffected = pstm1.executeUpdate();

                    if (rowAffected > 0){
                        System.out.println("Rs. "+ amount+ "Credit Successfully");
                        conn.commit();
                        conn.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed");
                        conn.rollback();
                        conn.setAutoCommit(true);
                    }

                }else {
                    System.out.println("Invalid Pin");
                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public long getBalance(long acc_num){
        System.out.println("Enter Security Pin: ");
        String pin = sc.next();
        String query = "SELECT balance FROM Accounts WHERE acc_num = ? AND security_pin = ?";
        try {
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.setLong(1, acc_num);
            pstm.setString(2,pin);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()){
                double balance = rs.getDouble("balance");
                System.out.println("Balance: "+ balance);
            }else {
                System.out.println("Invalid Pin");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account Number doesn't exist");
    }

    public void transfer_money(long sender_acc_num){
        sc.nextLine();
        System.out.println("Enter Receiver Account Number: ");
        long receiver_acc_num = sc.nextLong();
        System.out.println("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security Pin: ");
        String pin = sc.next();
        try{
            conn.setAutoCommit(false);
            if(sender_acc_num != 0 && receiver_acc_num != 0){
                PreparedStatement pstm = conn.prepareStatement("SELECT * FROM accounts WHERE acc_num = ? AND security_pin = ?");
                pstm.setLong(1,sender_acc_num);
                pstm.setString(2,pin);
                ResultSet rs = pstm.executeQuery();
                if(rs.next()){
                    double current_balance = rs.getDouble("balance");
                    if(current_balance >= amount){
                        String debit_query = "UPDATE Account SET balance = balance - ? WHERE acc_num = ?";
                        String credit_query = "UPDATE Account SET balance = balance + ? WHERE acc_num = ?";
                        PreparedStatement debitPstm = conn.prepareStatement(debit_query);
                        PreparedStatement creditPstm = conn.prepareStatement(credit_query);
                        debitPstm.setDouble(1,amount);
                        debitPstm.setLong(2,sender_acc_num);
                        creditPstm.setDouble(1,amount);
                        creditPstm.setLong(2,receiver_acc_num);
                        int rowAffected1 = debitPstm.executeUpdate();
                        int rowAffected2 = creditPstm.executeUpdate();
                        if(rowAffected1 > 0 && rowAffected2 > 0){
                            System.out.println("Transaction Successful!");
                            System.out.println("Rs." + amount + " Transferred Successfully");
                            conn.commit();
                            conn.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transaction Failed");
                            conn.rollback();
                            conn.setAutoCommit(true);
                        }

                    } else {
                        System.out.println("Insufficient amount");
                    }
                }else {
                    System.out.println("Invalid Security Pin");
                }
            }else {
                System.out.println("Invalid Account Number");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
