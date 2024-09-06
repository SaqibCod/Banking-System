import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Banking_App {

    public static final String url = "jdbc:mysql://localhost:3306/Banking_system";
    public static final String username = "root";
    public static final String password = "Saqib@12345";

    public static void main(String[] args) {

        try {
            Connection conn = DriverManager.getConnection(url,username,password);
            Scanner sc = new Scanner(System.in);
            Accounts account = new Accounts(conn,sc);
            Users user = new Users(conn,sc);
            AccountManager accountManager = new AccountManager(conn,sc);

            String email;
            long account_num;


            while (true){
                System.out.println("----Welcome to our BANK----");
                System.out.println("1. Register");
                System.out.println("2. login");
                System.out.println("3. Exit");
                System.out.println("Enter Your choice: ");
                int choice1 = sc.nextInt();
                switch (choice1){

                    case 1:
                        user.register();
                        break;
                    case 2:
                         email = user.login();
                        if (email != null){
                            System.out.println();
                            System.out.println("User Logged-in");
                            if(!account.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if (sc.nextInt() == 1){
                                    account_num = account.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Account Number: " + account_num);
                                }else {
                                    break;
                                }
                            }
                            account_num = account.getAccount_number(email);
                            int choice2 = 0;
                            while (choice2 != 5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter Choice: ");
                                choice2 = sc.nextInt();
                                switch (choice2){

                                    case 1:
                                        accountManager.debit_money(account_num);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_num);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_num);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_num);
                                        break;
                                    case 5:
                                        sc.close();
                                        return;

                                    default:
                                        System.out.println("Invalid Value Choose! Try again");
                                }
                            }
                        }else {
                            System.out.println("Incorrect email or password");
                        }
                        break;
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System...");
                        return;
                    default:
                        System.out.println("Invalid Choice!!!");
                }


            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
