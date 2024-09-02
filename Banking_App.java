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
                        System.out.flush();
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

                            }



                        }else {
                            System.out.println("Incorrect email or password");
                        }
                        break;
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System...");
                        return;
                }

            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
