package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.SQLHelper;
import ru.netology.web.page.PaymentTypeSelectionPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.SQLHelper.cleanDatabase;

public class CreditPaymentGateTest {

    PaymentTypeSelectionPage paymentType;
    String approvedCardNumber = DataHelper.getCardAPPROVED().getCardNumber();
    String declinedCardNumber = DataHelper.getCardDECLINED().getCardNumber();
    String validMonth = DataHelper.getRandomMonth(1);
    String validYear = DataHelper.getRandomYear(0);
    String validOwnerName = DataHelper.getRandomName();
    String validCode = DataHelper.getNumberCVC(3);

    String invalidCardNumber = DataHelper.getRandomCardNumberLess16();
    String nullCardNumber = DataHelper.getRandomCardNumberAllNulls();
    String emptyCardNumber = DataHelper.getEmptyField();

    String invalidMonthOneNumber = DataHelper.getInvalidMonthOneNumber("3");
    String nullMonth = DataHelper.getInvalidMonthNull();
    String emptyMonth = DataHelper.getEmptyField();
    String expiredMonth = DataHelper.getRandomMonth(-3);

    String expiredYear = DataHelper.getRandomYear(-2);
    String invalidYearOneNumber = DataHelper.getInvalidYearOneNumber("2");
    String emptyYear = DataHelper.getInvalidYearOneNumber("");
    String nullYear = DataHelper.getInvalidYearOneNumber("00");

    String emptyOwnerName = DataHelper.getEmptyField();
    String ruOwnerName = DataHelper.getRandomNameRU();
    String specialCharactersOwnerName = DataHelper.getInvalidSpecialCharactersName();
    String numberOwnerName = DataHelper.getInvalidNumberName();

    String emptyCVV = DataHelper.getEmptyField();
    String invalidCVVOneNumber = DataHelper.getNumberCVC(1);

    @BeforeAll
    static void setUPAll(){
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @BeforeEach
    void setUp() {
        paymentType = open("http://localhost:8080", PaymentTypeSelectionPage.class);
    }
    @AfterAll
    static void teardownAll() {
        cleanDatabase();
    }
    @AfterEach
    public void shouldCleanBase() {
        SQLHelper.cleanDatabase();
    }

    //Оплата в случае выбора Купить в кредит по данным карты со статусом APPROVED
    @Test
    public void shouldCardCreditPaymentApproved() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.findBankAPPROVEDOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCreditPayment());
    }

    //Оплата в случае выбора Купить в кредит по данным карты со статусом "DECLINED"
    //баг - Операция одобрена банком
    @Test
    public void shouldCardCreditCardPaymentDeclined() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(declinedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.findBankDeclinedOperation();
        Assertions.assertEquals("DECLINED", SQLHelper.getCreditPayment());
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, менее 16 цифр, значением номера карты
    @Test
    public void shouldCardPaymentCreditInvalidNumberCard() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(invalidCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, нулевым, значением номера карты
    //баг - Переход к операции, но Банк отказал в проведении операции
    @Test
    public void shouldCardPaymentCreditNullNumberCard() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(nullCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, пустым, значением номера карты
    @Test
    public void shouldCardPaymentCreditEmptyNumberCard() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(emptyCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, 1 цифра, значением месяца карты
    @Test
    public void shouldCardPaymentCreditInvalidMonthOneNumber() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, invalidMonthOneNumber, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, нулевым, значением месяца карты
    @Test
    public void shouldCardPaymentCreditNullMonth() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, nullMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorCardExpirationDate("Неверно указан срок действия карты");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, пустым, значением месяца карты
    @Test
    public void shouldCardPaymentCreditEmptyMonth() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, emptyMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным значением (истекшим сроком) месяца карты
    @Test
    public void shouldCardPaymentCreditExpiredMonth() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, expiredMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorCardExpirationDate("Неверно указан срок действия карты");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным значением (истекшим сроком действия) в поле "Год"
    @Test
    public void shouldCardPaymentCreditExpiredYear() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, expiredYear, validOwnerName, validCode);
        creditCardPage.errorCardExpired("Истёк срок действия карты");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным значением срока действия в поле "Год", 1 цифра
    @Test
    public void shouldCardPaymentCreditInvalidYearOneNumber() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, invalidYearOneNumber, validOwnerName, validCode);
        creditCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, пустым, значением срока действия в поле "Год"
    @Test
    public void shouldCardPaymentCreditInvalidEmptyYear() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, emptyYear, validOwnerName, validCode);
        creditCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, нулевым, значением срока действия в поле "Год"
    @Test
    public void shouldCardPaymentCreditInvalidNullYear() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, nullYear, validOwnerName, validCode);
        creditCardPage.errorCardExpired("Истёк срок действия карты");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, пустым, значением в поле "Владелец"
    @Test
    public void shouldCardPaymentCreditInvalidEmptyOwner() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, emptyOwnerName, validCode);
        creditCardPage.emptyField("Поле обязательно для заполнения");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, на кириллице, значением в поле "Владелец"
    //баг - Операция одобрена банком
    @Test
    public void shouldCardPaymentCreditInvalidRuOwner() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, ruOwnerName, validCode);
        creditCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, спец.символы, значением в поле "Владелец"
    //баг - Операция одобрена банком
    @Test
    public void shouldCardPaymentCreditInvalidSpecialCharactersOwner() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, specialCharactersOwnerName, validCode);
        creditCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, цифровым, значением в поле "Владелец"
    //баг - Операция одобрена банком
    @Test
    public void shouldCardPaymentCreditInvalidNumberOwner() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, numberOwnerName, validCode);
        creditCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, пустым, значением в поле "CVC/CVV"
    @Test
    public void shouldCardPaymentCreditInvalidEmptyCVV() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, emptyCVV);
        creditCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить в кредит по данным карты с невалидным, менее 3 цифр, значением в поле "CVC/CVV"
    @Test
    public void shouldCardPaymentCreditInvalidCVVOneNumber() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, invalidCVVOneNumber);
        creditCardPage.errorFormat("Неверный формат");
    }


    
}
