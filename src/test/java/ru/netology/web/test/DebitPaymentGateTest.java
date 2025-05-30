package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.SQLHelper;
import ru.netology.web.page.PaymentTypeSelectionPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.SQLHelper.cleanDatabase;

public class DebitPaymentGateTest {

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

    //Оплата в случае выбора Купить по карте со статусом APPROVED
    @Test
    public void shouldCardPaymentApproved() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitCardPage.findBankAPPROVEDOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCardPayment());
    }

    //Оплата в случае выбора Купить по карте со статусом "DECLINED"
    //баг - Операция одобрена банком
    @Test
    public void shouldCardPaymentDeclined() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(declinedCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitCardPage.findBankDeclinedOperation();
        Assertions.assertEquals("DECLINED", SQLHelper.getCardPayment());
    }

    //Оплата в случае выбора Купить по карте с невалидным, менее 16 цифр, значением номера карты
    @Test
    public void shouldCardPaymentInvalidNumberCard() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(invalidCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить по карте с невалидным, нулевым, значением номера карты
    //баг - Переход к операции, но Банк отказал в проведении операции
    @Test
    public void shouldCardPaymentNullNumberCard() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(nullCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить по карте с невалидным, пустым, значением номера карты
    @Test
    public void shouldCardPaymentEmptyNumberCard() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(emptyCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить по карте с невалидным, 1 цифра, значением месяца карты
    @Test
    public void shouldCardPaymentInvalidMonthOneNumber() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, invalidMonthOneNumber, validYear, validOwnerName, validCode);
        debitCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить по карте с невалидным, нулевым, значением месяца карты
    @Test
    public void shouldCardPaymentNullMonth() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, nullMonth, validYear, validOwnerName, validCode);
        debitCardPage.errorCardExpirationDate("Неверно указан срок действия карты");
    }

    //Оплата в случае выбора Купить по карте с невалидным, пустым, значением месяца карты
    @Test
    public void shouldCardPaymentEmptyMonth() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, emptyMonth, validYear, validOwnerName, validCode);
        debitCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить по карте с невалидным значением (истекшим сроком) месяца карты
    @Test
    public void shouldCardPaymentExpiredMonth() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, expiredMonth, validYear, validOwnerName, validCode);
        debitCardPage.errorCardExpirationDate("Неверно указан срок действия карты");
    }

    //Оплата в случае выбора Купить по карте с невалидным значением (истекшим сроком действия) в поле "Год"
    @Test
    public void shouldCardPaymentExpiredYear() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, expiredYear, validOwnerName, validCode);
        debitCardPage.errorCardExpired("Истёк срок действия карты");
    }

    //Оплата в случае выбора Купить картой с невалидным значением срока действия в поле "Год", 1 цифра
    @Test
    public void shouldCardPaymentInvalidYearOneNumber() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, invalidYearOneNumber, validOwnerName, validCode);
        debitCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить картой с невалидным, пустым, значением срока действия в поле "Год"
    @Test
    public void shouldCardPaymentInvalidEmptyYear() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, emptyYear, validOwnerName, validCode);
        debitCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить картой с невалидным, нулевым, значением срока действия в поле "Год"
    @Test
    public void shouldCardPaymentInvalidNullYear() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, nullYear, validOwnerName, validCode);
        debitCardPage.errorCardExpired("Истёк срок действия карты");
    }

    //Оплата в случае выбора Купить картой с невалидным, пустым, значением в поле "Владелец"
    @Test
    public void shouldCardPaymentInvalidEmptyOwner() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, emptyOwnerName, validCode);
        debitCardPage.emptyField("Поле обязательно для заполнения");
    }

    //Оплата в случае выбора Купить картой с невалидным, на кириллице, значением в поле "Владелец"
    //баг - Операция одобрена банком
    @Test
    public void shouldCardPaymentInvalidRuOwner() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, ruOwnerName, validCode);
        debitCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить картой с невалидным, спец.символы, значением в поле "Владелец"
    //баг - Операция одобрена банком
    @Test
    public void shouldCardPaymentInvalidSpecialCharactersOwner() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, specialCharactersOwnerName, validCode);
        debitCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить картой с невалидным, цифровым, значением в поле "Владелец"
    //баг - Операция одобрена банком
    @Test
    public void shouldCardPaymentInvalidNumberOwner() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, numberOwnerName, validCode);
        debitCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить по карте с невалидным, пустым, значением в поле "CVC/CVV"
    @Test
    public void shouldCardPaymentInvalidEmptyCVV() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, emptyCVV);
        debitCardPage.errorFormat("Неверный формат");
    }

    //Оплата в случае выбора Купить по карте с невалидным, менее 3 цифр, значением в поле "CVC/CVV"
    @Test
    public void shouldCardPaymentInvalidCVVOneNumber() {
        PaymentTypeSelectionPage page = new PaymentTypeSelectionPage();
        page.paymentTypeSelectionPage();
        var debitCardPage = page.debitPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillingCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, invalidCVVOneNumber);
        debitCardPage.errorFormat("Неверный формат");
    }


}
