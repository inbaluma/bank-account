package bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankAccountTest {

    private BankAccount bankAccount;
    private static final double EPSILON = 0.0001;

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
    public void deposit_overMaximumAmount_throwsError() {
        // We try to deposit the maximum amount allowed per account
        int deposit = bankAccount.getMaximumAmount() + 100;

        // This throws an expection because a bank account cannot surpass the maximum
        // amount allowed
        assertThrows(IllegalStateException.class, () -> {
            bankAccount.deposit(deposit);
        });

        // The account's balance should not change
        assertEquals(100, bankAccount.getBalance());
    }

    @Test
    public void deposit_balanceOverMaximumAmount_throwsError() {
        int deposit = bankAccount.getMaximumAmount()/2;

        bankAccount.deposit(deposit);

        // With the second deposit, the balance would surpass the maximum allowed
        assertThrows(IllegalStateException.class, () -> {
            bankAccount.deposit(deposit);
        });

        // The account's balance should not change after first deposit
        assertEquals(100 + deposit, bankAccount.getBalance());
    }

    @Test
    public void deposit_negativeValue_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.deposit(-50);
        });

        // The account's balance should not change
        assertEquals(100, bankAccount.getBalance());
    }

    
    @Test
    public void payment_positiveArguments_correctValue() {
        // Arguments for the function
        double total = 10000;
        double interest = 0.002;
        int n = 12;

        double payment = bankAccount.payment(total, interest, n);

        // We invert the interest to every month's payment and calculate the total
        // This value has to be the same as the initial amount of the loan
        double res = 0;
        double currentInterest = 1;
        for (int i = 0; i < n; i++) {
            currentInterest *= 1 + interest;
            res += payment / currentInterest;
        }
        // Approximate value according to an epsilon
        assertTrue(approximatedTo(res, total));
    }

    private static boolean approximatedTo(double value, double approximatedValue) {
        return Math.abs(value - approximatedValue) <= EPSILON;
    }

    @Test
    public void payment_negativeTotal_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.payment(-100, 0.1, 15);
        });
    }

    @Test
    public void payment_negativeInterest_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.payment(100, -0.1, 15);
        });
    }

    @Test
    public void payment_negativePayments_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.payment(100, 0.1, -15);
        });
    }

    @Test
    public void payment_zeroPayments_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.payment(100, 0.1, 0);
        });
    }

    @Test
    public void payment_overMaximumTotal_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.payment(bankAccount.getMaximumAmount() + 500, 0.1, 10);
        });
    }

    @Test
    public void payment_overMaximumInterest_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.payment(100, bankAccount.getMaximumInterest() + 0.1, 15);
        });
    }


    @Test
    public void pending_firstMonthOfPayment_returnsTotalAmountWithInterestMinusPayment() {
        double amount = 100;
        double interest = 0.1;
        int n = 12;
        int month = 1;

        double pending = bankAccount.pending(amount, interest, n, month);
        double payment = bankAccount.payment(amount, interest, n);

        assertTrue(approximatedTo(amount*(1+interest) - payment, pending));
    }

    @Test
    public void pending_monthStartingLoan_returnsTotalAmount() {
        double amount = 100;
        double interest = 0.1;
        int n = 12;
        int month = 0;

        double pending = bankAccount.pending(amount, interest, n, month);

        assertTrue(approximatedTo(amount, pending));
    }
    
    @Test
    public void pending_lastMonthOfLoan_returnsZeroAmountPending() {
        double amount = 100;
        double interest = 0.1;
        int n = 12;
        int month = 12;

        double pending = bankAccount.pending(amount, interest, n, month);
        
        assertTrue(approximatedTo(0, pending));
    }
    

    @Test
    public void pending_negativeTotal_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.pending(-100, 0.1, 15, 2);
        });
    }

    @Test
    public void pending_negativeInterest_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.pending(100, -0.1, 15, 2);
        });
    }

    @Test
    public void pending_negativePayments_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.pending(100, 0.1, -15, 2);
        });
    }

    @Test
    public void pending_zeroPayments_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.pending(100, 0.1, 0, 2);
        });
    }

    @Test
    public void pending_negativeMonth_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.pending(100, 0.1, 15, -2);
        });
    }

    @Test
    public void pending_overMaximumTotal_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.pending(bankAccount.getMaximumAmount() + 500, 0.1, 10, 2);
        });
    }

    @Test
    public void pending_overMaximumInterest_throwsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            bankAccount.pending(100, bankAccount.getMaximumInterest() + 0.1, 15, 2);
        });
    }
}