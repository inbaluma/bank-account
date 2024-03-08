package bank;

import org.junit.jupiter.api.Test;

public class BankMainTest {

    @Test
    public void main_createBankMain() {
        BankMain bankMain = new BankMain();
    }

    @Test
    public void main_callMain() {
        BankMain.main(null);
    }
}
