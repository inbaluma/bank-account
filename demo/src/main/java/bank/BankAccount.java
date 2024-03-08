package bank;

public class BankAccount {

    private static final int MAXIMUM_AMOUNT = 1000000000; // A billion units
    public static final double MAXIMUM_INTEREST = 10.5;

    private int balance = 0;

    public BankAccount(int startingBalance) {
        this.balance = startingBalance;
    }

    public int getMaximumAmount() {
        return MAXIMUM_AMOUNT;
    }

    public double getMaximumInterest() {
        return MAXIMUM_INTEREST;
    }

    public boolean withdraw(int amount) {
        if (balance >= amount && amount >= 0) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public int deposit(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount cannot be negative");
        if (amount > MAXIMUM_AMOUNT || amount + balance > MAXIMUM_AMOUNT) {
            throw new IllegalStateException("Balance cannot surpass maximum value of " + MAXIMUM_AMOUNT);
        }
        balance += amount;
        return balance;
    }

    public int getBalance() {
        return balance;
    }

    // Calculate the payment per month for a loan
    public double payment(double total_amount, double interest, int npayments) {
        if (total_amount < 0 || interest < 0 || npayments <= 0) {
            throw new IllegalArgumentException(
                    "The total amount for the loan, the interest and the number of payments have to be positive");
        }
        if (total_amount > MAXIMUM_AMOUNT) {
            throw new IllegalArgumentException(
                    "The total initial amount for a loan cannot surpass the value of " + MAXIMUM_AMOUNT);
        }
        if (interest > MAXIMUM_INTEREST) {
            throw new IllegalArgumentException("The maximum interest allowed is " + MAXIMUM_INTEREST);
        }
        return total_amount
                * (interest * Math.pow((1 + interest), npayments) / (Math.pow((1 + interest), npayments) - 1));
    }

    // Calculate the pending amount for a loan in a month
    public double pending(double amount, double inte, int npayments, int month) {
        if (amount < 0 || inte < 0 || npayments <= 0 || month < 0) {
            throw new IllegalArgumentException(
                    "The total amount for the loan, the interest, the number of payments and the current month have to be positive");
        }
        if (amount > MAXIMUM_AMOUNT) {
            throw new IllegalArgumentException(
                    "The total initial amount for a loan cannot surpass the value of " + MAXIMUM_AMOUNT);
        }
        if (inte > MAXIMUM_INTEREST) {
            throw new IllegalArgumentException("The maximum interest allowed is " + MAXIMUM_INTEREST);
        }
        double res;
        if (month == 0) {
            res = amount;
        } else if (month >= npayments) {
            res = 0;
        }
        else
        {
            double ant = pending(amount, inte, npayments, month - 1);
            res = ant - (payment(amount, inte, npayments) - inte * ant);
        }
        return res;
    }
}
