package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class PaymentTypeSelectionPage {

    private SelenideElement heading = $(byText("Путешествие дня"));
    private SelenideElement debitButton = $(byText("Купить"));
    private SelenideElement creditButton = $(byText("Купить в кредит"));

    public void paymentTypeSelectionPage() {
            heading.shouldBe(visible);
    }

    public DebitCardPage debitPayment() {
            debitButton.click();
            return new DebitCardPage();
    }

    public CreditCardPage creditPayment() {
            creditButton.click();
            return new CreditCardPage();
    }

}
