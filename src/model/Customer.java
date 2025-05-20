package model;

/**
 * Customer class represents a bank user with an account balance.
 */
public class Customer extends User {
    private double balance;

    public Customer(int id, String username, String password, double balance) {
        super(id, username, password);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
