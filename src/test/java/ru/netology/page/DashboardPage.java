package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.w3c.dom.html.HTMLInputElement;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");

    public static SelenideElement card1 = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']");
    public static SelenideElement card2 = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']");

    public DashboardPage() {
        heading.shouldBe(visible);
            }
    public static TransferPage pushCardButton (SelenideElement card) {
        card.find("button[data-test-id=action-deposit]").click();
        return new TransferPage();
    }

    public int getCardBalance(SelenideElement card) {
        String [] text = card.innerText().split(" ");
        return Integer.parseInt(text[5]);
    }

}
