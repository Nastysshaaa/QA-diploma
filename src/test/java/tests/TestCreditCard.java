package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import data.DataHelper;
import data.SQLRequests;
import page.PaymentPage;


import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static data.DataHelper.*;
import static data.DataHelper.emptyCvv;
import static data.SQLRequests.clearTables;

public class TestCreditCard {
    private PaymentPage paymentPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {

        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        paymentPage = new PaymentPage();
    }

    @AfterEach
    public void cleanTables() {
        clearTables();
    }

    private void fillOtherFieldsValidInfo() {
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(DataHelper.getYear(0));
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(DataHelper.getRandomValidCvv());
        paymentPage.clickContinueButton();
    }

    private void stayAllFieldsEmpty() {
        paymentPage.fillCardNumberField(emptyCardNumber);
        paymentPage.fillMonthField(emptyMonth);
        paymentPage.fillYearField(emptyYear);
        paymentPage.fillHolderField(emptyName);
        paymentPage.fillCvvField(emptyCvv);
        paymentPage.clickContinueButton();
    }

    @Test
    void shouldCheckValidApprovedByCreditCard() {
        paymentPage.openCreditCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.shouldHaveSuccessNotification();
        fillOtherFieldsValidInfo();
        assertEquals("APPROVED", SQLRequests.getStatusByCredit());
    }

    @Test
    void shouldCheckValidDeclinedCreditCard() {
        paymentPage.openCreditCardPaymentPage();
        paymentPage.fillCardNumberField(declinedCardNumber);
        paymentPage.shouldHaveErrorNotification();
        fillOtherFieldsValidInfo();
        assertEquals("DECLINED", SQLRequests.getStatusByCredit());
    }

    @Test
    void shouldCheckInvalidByCreditCard() {
        paymentPage.openCreditCardPaymentPage();
        paymentPage.fillCardNumberField(getRandomCard());
        paymentPage.shouldHaveErrorNotification();
        fillOtherFieldsValidInfo();
        assertNull(SQLRequests.getStatusByCredit());
    }

    @Test
    void shouldCheckEmptyByCreditCard() {
        paymentPage.openCreditCardPaymentPage();
        stayAllFieldsEmpty();
        paymentPage.shouldHaveErrorNotificationRequiredField();
        $(byText("Поле обязательно для заполнения")).shouldBe(visible, Duration.ofSeconds(30));
        ;
    }

    @Test
    void shouldCheckZeroValuesByCreditCard() {
        paymentPage.openCreditCardPaymentPage();
        paymentPage.fillCardNumberField(zeroCardNumber);
        paymentPage.fillMonthField(zeroMonth);
        paymentPage.fillYearField(zeroYear);
        paymentPage.fillHolderField(zeroName);
        paymentPage.fillCvvField(DataHelper.getRandomValidCvv());
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotification();
        assertNull(SQLRequests.getStatusByCredit());
    }

    @Test
    void shouldCheckShortCardNumberByCreditCard() {
        paymentPage.openCreditCardPaymentPage();
        paymentPage.fillCardNumberField(shortCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(DataHelper.getYear(0));
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(DataHelper.getRandomValidCvv());
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotificationWrongFormat();
        assertNull(SQLRequests.getStatusByCredit());

    }

    @Test
    void shouldCheckInvalidMonthCard() { //проверка неверного значения по месяцу карты
        paymentPage.openCreditCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(invalidMonth);
        paymentPage.fillYearField(DataHelper.getYear(0));  //()
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(getRandomValidCvv());
        paymentPage.clickContinueButton();
        $(byText("Неверный формат")).shouldBe(visible, Duration.ofSeconds(30));
    }


    @Test
    void shouldCheckInvalidHolderByCreditCard() {
        paymentPage.openCreditCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(DataHelper.getYear(0));
        paymentPage.fillHolderField(invalidHolder);
        paymentPage.fillCvvField(DataHelper.getRandomValidCvv());
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotificationWrongFormat();
        assertNull(SQLRequests.getStatusByCredit());
    }

    @Test
    void shouldCheckFalseMonthByCreditCard() {
        paymentPage.openCreditCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(invalidMonth);
        paymentPage.fillYearField(DataHelper.getYear(0));
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(DataHelper.getRandomValidCvv());
        paymentPage.clickContinueButton();
        $(byText("Неверно указан срок действия карты")).shouldBe(visible, Duration.ofSeconds(30));
    }

    @Test
    void shouldCheckLastYearCreditCard() {
        paymentPage.openCreditCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(lastYear);
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(DataHelper.getRandomValidCvv());
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotificationCardExpired();
        $(byText("Истёк срок действия карты")).shouldBe(visible, Duration.ofSeconds(30));
    }

    @Test
    void shouldCheckInvalidCvvByCreditCard() {
        paymentPage.openCreditCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(DataHelper.getYear(0));
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(invalidCvv);
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotificationWrongFormat();
        assertNull(SQLRequests.getStatusByCredit());
    }

}
