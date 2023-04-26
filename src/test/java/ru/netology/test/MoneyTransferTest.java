package ru.netology.test;

import com.codeborne.selenide.Configuration;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getFirstCardNumber;
import static ru.netology.data.DataHelper.getSecondCardNumber;
import static ru.netology.page.DashboardPage.pushFirstCardButton;
import static ru.netology.page.DashboardPage.pushSecondCardButton;

public class MoneyTransferTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val cardBalance = verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldTransferMoneyFromFirstToSecond() {
        int amount = 1000;

        val cardBalance = new DashboardPage();
        val firstCardBalanceStart = cardBalance.getFirstCardBalance();
        val secondCardBalanceStart = cardBalance.getSecondCardBalance();
        val transferPage = pushSecondCardButton();
        transferPage.transferMoney(amount, getFirstCardNumber());
        val firstCardBalanceFinish = firstCardBalanceStart - amount;
        val secondCardBalanceFinish = secondCardBalanceStart + amount;
        assertEquals(firstCardBalanceFinish, cardBalance.getFirstCardBalance());
        assertEquals(secondCardBalanceFinish, cardBalance.getSecondCardBalance());

    }
    @Test
    public void shouldTransferMoneyFromSecondToFirst() {
        int amount = 2000;

        val cardBalance = new DashboardPage();
        val firstCardBalanceStart = cardBalance.getFirstCardBalance();
        val secondCardBalanceStart = cardBalance.getSecondCardBalance();
        val transferPage = pushFirstCardButton();
        transferPage.transferMoney(amount, getSecondCardNumber());
        val firstCardBalanceFinish = firstCardBalanceStart + amount;
        val secondCardBalanceFinish = secondCardBalanceStart - amount;
        assertEquals(firstCardBalanceFinish, cardBalance.getFirstCardBalance());
        assertEquals(secondCardBalanceFinish, cardBalance.getSecondCardBalance());

    }
    @Test
    public void shouldTransferFromFirstToFirst() {
        int amount = 555;
        val cardBalance = new DashboardPage();
        val firstCardBalanceStart = cardBalance.getFirstCardBalance();
        val transferPage = pushFirstCardButton();
        transferPage.transferMoney(amount, getFirstCardNumber());
        transferPage.invalidCard();
    }
    @Test
    public void shouldTransferFromSecondToSecond() {
        int amount = 777;
        val cardBalance = new DashboardPage();
        val secondCardBalanceStart = cardBalance.getSecondCardBalance();
        val transferPage = pushSecondCardButton();
        transferPage.transferMoney(amount, getSecondCardNumber());
        transferPage.invalidCard();
    }
    @Test
    public void shouldTransferFromFirstToSecondOverBalance() {
        int amount = 11000;
        val cardBalance = new DashboardPage();
        val firstCardBalanceStart = cardBalance.getFirstCardBalance();
        val secondCardBalanceStart = cardBalance.getSecondCardBalance();
        val transferPage = pushSecondCardButton();
        transferPage.transferMoney(amount, getFirstCardNumber());
        transferPage.errorLimit();
    }
}
