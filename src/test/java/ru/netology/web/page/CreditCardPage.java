package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditCardPage {

    private SelenideElement heading = $(byText("Кредит по данным карты"));
    private final SelenideElement cardNumber = $("[placeholder='0000 0000 0000 0000']");
    private final SelenideElement month = $("[placeholder='08']");
    private final SelenideElement year = $("[placeholder='22']");
    private final SelenideElement owner = $$(".input__inner").findBy(text("Владелец")).$(".input__control");
    private final SelenideElement code = $("[placeholder='999']");
    private final SelenideElement continueButton = $$("button").findBy(text("Продолжить"));

    public CreditCardPage() {
        heading.shouldBe(visible);
    }

    public void fillingCardPaymentForm(String cardNumberValue, String monthValue, String yearValue, String ownerValue, String codeValue) {
        cardNumber.setValue(cardNumberValue);
        month.setValue(monthValue);
        year.setValue(yearValue);
        owner.setValue(ownerValue);
        code.setValue(codeValue);
        continueButton.click();
    }

    private final SelenideElement bankAPPROVED = $$(".notification__content").findBy(text("Операция одобрена Банком."));
    private final SelenideElement bankDECLINED = $$(".notification__content").findBy(text("Ошибка! Банк отказал в проведении операции."));
    private final SelenideElement invalidFormat = $$(".input__inner").findBy(text("Неверный формат"));
    private final SelenideElement emptyField = $$(".input__inner").findBy(text("Поле обязательно для заполнения"));
    private final SelenideElement cardExpirationDateIncorrect = $$(".input__inner").findBy(text("Неверно указан срок действия карты"));
    private final SelenideElement cardExpired = $$(".input__inner").findBy(text("Истёк срок действия карты"));


    public void findBankAPPROVEDOperation() {
        bankAPPROVED.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void findBankDeclinedOperation() {
        bankDECLINED.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void errorFormat(String expectedText) {
        invalidFormat.shouldHave(text(expectedText)).shouldBe(visible);
    }

    public void errorCardExpirationDate(String expectedText) {
        cardExpirationDateIncorrect.shouldHave(text(expectedText)).shouldBe(visible);
    }

    public void emptyField(String expectedText) {
        emptyField.shouldHave(text(expectedText)).shouldBe(visible);
    }

    public void errorCardExpired(String expectedText) {
        cardExpired.shouldHave(text(expectedText)).shouldBe(visible);
    }

    public void cleanFields() {
        cardNumber.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        month.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        year.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        owner.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        code.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    }

}
