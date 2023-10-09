package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLRequests;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import page.PaymentPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.*;
import static data.SQLRequests.clearTables;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class TestPurchaseCard {
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

    @Test
    void shouldCheckValidApprovedByCard() { //проверка одобренной карты 
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(DataHelper.getYear(0));
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(getRandomValidCvv());
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveSuccessNotification();

        assertEquals("APPROVED", SQLRequests.getStatusByCard());
    }
    @Test
    void shouldCheckValidDeclinedByCard() { //проверка отклненной карты
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(declinedCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(DataHelper.getYear(0));
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(getRandomValidCvv());
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotification();

        assertEquals("DECLINED", SQLRequests.getStatusByCard());
    }

    @Test
    void shouldCheckEmptyByCard() {  //проверка пустого поля карты
        paymentPage.openCardPaymentPage();
        stayAllFieldsEmpty();
        $(byText ("Поле обязательно для заполнения")).shouldBe(visible, Duration.ofSeconds(30));
    }

    @Test
    void shouldCheckInvalidByCard() {  //проверка неверной карты
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(getRandomCard());
        paymentPage.fillCardNumberField(declinedCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(DataHelper.getYear(0));
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(getRandomValidCvv());
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotification();

        assertNull(SQLRequests.getStatusByCard());
    }
    @Test
    void shouldCheckZeroValuesByCard() { //проверка нулевых значений по карте
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(zeroCardNumber);
        paymentPage.fillMonthField(zeroMonth);
        paymentPage.fillYearField(zeroYear);
        paymentPage.fillHolderField(zeroName);
        paymentPage.fillCvvField(getRandomValidCvv());
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotification();
        assertNull(SQLRequests.getStatusByCard());
    }
    @Test
    void shouldCheckShortCardNumber() { //проверка короткого значения по карте
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(zeroCardNumber);
        paymentPage.fillMonthField(zeroMonth);
        paymentPage.fillYearField(zeroYear);
        paymentPage.fillHolderField(zeroName);
        paymentPage.fillCvvField(getRandomValidCvv());
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotification();
        assertNull(SQLRequests.getStatusByCard());
    }

    @Test
    void shouldCheckInvalidMonthCard() { //проверка неверного значения по месяцу карты
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(invalidMonth);
        paymentPage.fillYearField(DataHelper.getYear(24));  //()
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(getRandomValidCvv());
        paymentPage.clickContinueButton();
        $(byText ("Неверный формат")).shouldBe(visible, Duration.ofSeconds(30));
    }
    @Test
    void shouldCheckInvalidYearCard() { //проверка неверного значения по году действия карты
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(invalidYear);
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(getRandomValidCvv());
        paymentPage.clickContinueButton();
        $(byText ("Неверный формат")).shouldBe(visible, Duration.ofSeconds(30));
    }
    @Test
    void shouldCheckLastYearByCard() {  //проверка срока действия карты, прошлого года
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(lastYear);
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(getRandomValidCvv());
        paymentPage.clickContinueButton();
        $(byText ("Истёк срок действия карты")).shouldBe(visible, Duration.ofSeconds(30));

    }
    @Test
    void shouldCheckFalseMonthCard() {  //проверка  неверно заполненного поля "месяц"
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(invalidMonth);
        paymentPage.fillYearField(DataHelper.getYear(0));
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(getRandomValidCvv());
        paymentPage.clickContinueButton();
        $(byText ("Неверно указан срок действия карты")).shouldBe(visible, Duration.ofSeconds(30));
    }
    @Test
    void shouldCheckInvalidHolderCard() {  //проверка неверно заполненного поля "владелец"
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(DataHelper.getYear(0));
        paymentPage.fillHolderField(invalidHolder);
        paymentPage.fillCvvField(getRandomValidCvv());
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotificationWrongFormat();
        assertNull(SQLRequests.getStatusByCard());
    }
    @Test
    void shouldCheckInvalidCvsByCard() { //проверка неверно заполненного поля "cvv"
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(DataHelper.getYear(0));
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvvField(invalidCvv);
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotificationWrongFormat();
        assertNull(SQLRequests.getStatusByCard());
    }

    private void stayAllFieldsEmpty() {  //оставить все поля пустыми
        paymentPage.fillCardNumberField(emptyCardNumber);
        paymentPage.fillMonthField(emptyMonth);
        paymentPage.fillYearField(emptyYear);
        paymentPage.fillHolderField(emptyName);
        paymentPage.fillCvvField(emptyCvv);
        paymentPage.clickContinueButton();
    }
}
