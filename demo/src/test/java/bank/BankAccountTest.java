package bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankAccountTest {

    BankAccount bankAccount;

    @BeforeEach
    public void initialize() {
        bankAccount = new BankAccount(100);
    }


    @Test
    public void withdraw_validAmount_accountWithdrawsMoney() {
        // We withdraw 50 units from an account with a balance of 100 units
        bankAccount.withdraw(50);

        // The account should have a balance of 50 units after the withdrawal
        assertEquals(50, bankAccount.getBalance());
    }
    
    @Test
    public void withdraw_higherThanBalance_accountDoesntWithdrawMoney() {
        // We try to withdraw 150 units from an account with a balance of 100 units
        bankAccount.withdraw(150);

        // The account should still have the initial balance of 100 units
        assertEquals(100, bankAccount.getBalance());
    }

    @Test
    public void withdraw_negativeValue_accountDoesntWithdrawMoney() {
        // We try to withdraw -50 units from an account with a balance of 100 units
        bankAccount.withdraw(-50);

        // The account should still have the initial balance of 100 units
        assertEquals(100, bankAccount.getBalance());
    }


    @Test
    public void deposit_validAmount_accountAcceptsDeposit() {
        // We deposit 50 units into an account with a balance of 100 units
        int balance = bankAccount.deposit(50);

        // The account should have the new balance of 150 units
        assertEquals(150, balance);
    }
    
    @Test
    public void deposit_overMaximumAmount_thowsError() {
        // We try to deposit the maximum amount allowed per account
        int deposit = BankAccount.MAXIMUM_AMOUNT;

        // This throws an expection because a bank account cannot surpass the maximum amount allowed
        assertThrows(IllegalStateException.class, ()->{
            bankAccount.deposit(deposit);
        });

        // The account's balance should not change
        assertEquals(100, bankAccount.getBalance());
    }

    @Test
    public void deposit_negativeValue_throwsError() {
        assertThrows(IllegalArgumentException.class, ()->{
            bankAccount.deposit(-50);
        });

        // The account's balance should not change
        assertEquals(100, bankAccount.getBalance());
    }

}