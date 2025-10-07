import java.sql.*;
import java.util.*;

class Account {
    private int accountNo;
    private String holderName;
    private String type;
    private double balance;
    private boolean active;

    public Account(int accountNo, String holderName, double balance, String type, boolean active) {
        this.accountNo = accountNo;
        this.holderName = holderName;
        this.type = type;
        this.balance = balance;
        this.active = active;
    }

    public int getAccountNo() { return accountNo; }
    public String getHolderName() { return holderName; }
    public String getType() { return type; }
    public double getBalance() { return balance; }
    public boolean isActive() { return active; }
}

class AccountManager {
    private final String URL = "jdbc:mysql://localhost:3306/bankdb?useSSL=false&serverTimezone=UTC";
    private final String USER = "root"; // change to your MySQL username
    private final String PASS = "bntyipo43505408^%^*%%^&^&fghhfhh5y56877"; // change to your MySQL password

    public Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public int getNextAccountNo() {
        int nextNo = 1001;
        try (Connection con = connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(accountNo) FROM accounts")) {
            if (rs.next() && rs.getInt(1) != 0) {
                nextNo = rs.getInt(1) + 1;
            }
        } catch (Exception e) {
            System.out.println("Error fetching next account number: " + e.getMessage());
        }
        return nextNo;
    }

    public void addAccount(Account acc) {
        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO accounts (accountNo, holderName, type, balance, active) VALUES (?, ?, ?, ?, ?)")) {
            ps.setInt(1, acc.getAccountNo());
            ps.setString(2, acc.getHolderName());
            ps.setString(3, acc.getType());
            ps.setDouble(4, acc.getBalance());
            ps.setBoolean(5, acc.isActive());
            ps.executeUpdate();
            System.out.println("Account created. Acc No: " + acc.getAccountNo());
        } catch (Exception e) {
            System.out.println("Error adding account: " + e.getMessage());
        }
    }

    public Account findAccount(int accNo) {
        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE accountNo=? AND active=true")) {
            ps.setInt(1, accNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("accountNo"),
                        rs.getString("holderName"),
                        rs.getDouble("balance"),
                        rs.getString("type"),
                        rs.getBoolean("active")
                );
            }
        } catch (Exception e) {
            System.out.println("Error finding account: " + e.getMessage());
        }
        return null;
    }

    public void updateBalance(int accNo, double newBalance) {
        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE accounts SET balance=? WHERE accountNo=?")) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, accNo);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error updating balance: " + e.getMessage());
        }
    }

    public void showAllAccounts() {
        try (Connection con = connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM accounts")) {
            System.out.println("Acc No\tName\tType\tBalance\tStatus");
            while (rs.next()) {
                System.out.printf(
                        "%d\t%s\t%s\t%.2f\t%s%n",
                        rs.getInt("accountNo"),
                        rs.getString("holderName"),
                        rs.getString("type"),
                        rs.getDouble("balance"),
                        rs.getBoolean("active") ? "Active" : "Inactive"
                );
            }
        } catch (Exception e) {
            System.out.println("Error showing accounts: " + e.getMessage());
        }
    }
}

public class Banking_system_with_db {
    private static Scanner sc = new Scanner(System.in);
    private static AccountManager manager = new AccountManager();

    public static void main(String[] args) {
        boolean run = true;
        while (run) {
            System.out.println("\n--- Banking System Menu ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Show All Accounts");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    manager.showAllAccounts();
                    break;
                default:
                    run = false;
            }
        }
    }

    private static void createAccount() {
        try {
            System.out.print("Enter Name: ");
            String name = sc.next();
            System.out.print("Account Type (Savings/Checking): ");
            String type = sc.next();
            System.out.print("Initial Deposit: ");
            double deposit = sc.nextDouble();
            int accNo = manager.getNextAccountNo();
            Account acc = new Account(accNo, name, deposit, type, true);
            manager.addAccount(acc);
        } catch (Exception e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    private static void deposit() {
        try {
            System.out.print("Enter Account Number: ");
            int accNo = sc.nextInt();
            Account acc = manager.findAccount(accNo);
            if (acc != null) {
                System.out.print("Enter Amount: ");
                double amt = sc.nextDouble();
                double newBal = acc.getBalance() + amt;
                manager.updateBalance(accNo, newBal);
                System.out.println("Deposited. Current Balance: " + newBal);
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception e) {
            System.out.println("Error during deposit: " + e.getMessage());
        }
    }

    private static void withdraw() {
        try {
            System.out.print("Enter Account Number: ");
            int accNo = sc.nextInt();
            Account acc = manager.findAccount(accNo);
            if (acc != null) {
                System.out.print("Enter Amount: ");
                double amt = sc.nextDouble();
                if (amt > 0 && amt <= acc.getBalance()) {
                    double newBal = acc.getBalance() - amt;
                    manager.updateBalance(accNo, newBal);
                    System.out.println("Withdrawn. Current Balance: " + newBal);
                } else {
                    System.out.println("Insufficient balance or invalid amount.");
                }
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception e) {
            System.out.println("Error during withdrawal: " + e.getMessage());
        }
    }
}
