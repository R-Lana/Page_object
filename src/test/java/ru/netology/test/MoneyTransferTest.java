package ru.netology.test;

import com.codeborne.selenide.Configuration;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getFirstCardNumber;
import static ru.netology.data.DataHelper.getSecondCardNumber;
import static ru.netology.page.DashboardPage.*;

public class MoneyTransferTest {
    int firstCardBalanceStart;
    int secondCardBalanceStart;
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCardBalanceStart = dashboardPage.getCardBalance(dashboardPage.card1);
        secondCardBalanceStart = dashboardPage.getCardBalance(dashboardPage.card2);

    }

    @Test
    public void shouldTransferMoneyFromFirstToSecond() {
        int amount = 1000;
        val cardBalance = dashboardPage;
        val transferPage = pushCardButton(dashboardPage.card2);
        transferPage.transferMoney(amount, getFirstCardNumber());
        val firstCardBalanceFinish = firstCardBalanceStart - amount;
        val secondCardBalanceFinish = secondCardBalanceStart + amount;
        assertEquals(firstCardBalanceFinish, cardBalance.getCardBalance(card1));
        assertEquals(secondCardBalanceFinish, cardBalance.getCardBalance(card2));

    }
    @Test
    public void shouldTransferMoneyFromSecondToFirst() {
        int amount = 2000;
        val cardBalance = dashboardPage;
        val transferPage = pushCardButton(dashboardPage.card1);
        transferPage.transferMoney(amount, getSecondCardNumber());
        val firstCardBalanceFinish = firstCardBalanceStart + amount;
        val secondCardBalanceFinish = secondCardBalanceStart - amount;
        assertEquals(firstCardBalanceFinish, cardBalance.getCardBalance(card1));
        assertEquals(secondCardBalanceFinish, cardBalance.getCardBalance(card2));

    }
    @Test
    public void shouldTransferMoneyFromSecondToSecond() {
        int amount = 2000;
        val cardBalance = dashboardPage;
        val transferPage = pushCardButton(dashboardPage.card2);
        transferPage.transferMoney(amount, getSecondCardNumber());
        transferPage.unsuccessfulTransfer();
    }
    @Test
    public void shouldNotTransferMoreThanAvailable() {
        int amount = firstCardBalanceStart + 1;
        val cardBalance = dashboardPage;
        val transferPage = pushCardButton(dashboardPage.card2);
        transferPage.transferMoney(amount, getFirstCardNumber());
        transferPage.unsuccessfulTransfer();
    }
}
