package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLRequests;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.PaymentPage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.approvedCardNumber;
import static data.DataHelper.getRandomValidCVV;
import static data.SQLRequests.clearTables;
import static org.junit.jupiter.api.Assertions.assertEquals;


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
        paymentPage.fillCvcField(getRandomValidCVV());
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
        paymentPage.fillCvcField(getRandomValidCVV());
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
        paymentPage.fillCvcField(getRandomValidCVV());
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotification();

        assertNull(SQLRequests.getStatusByCard());
    }
    @Test
    void shouldCheckZeroValuesByCard() { //проверка нулевых значений по карте
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(zeroCard);
        paymentPage.fillMonthField(zeroMonth);
        paymentPage.fillYearField(zeroYear);
        paymentPage.fillHolderField(zeroName);
        paymentPage.fillCVVField(getRandomValidCVV());
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotification();
        assertNull(SQLRequests.getStatusByCard());
    }
    @Test 
    void shouldCheckShortCardNumber() { //проверка короткого значения по карте
           paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(zeroCard);
        paymentPage.fillMonthField(zeroMonth);
        paymentPage.fillYearField(zeroYear);
        paymentPage.fillHolderField(zeroName);
        paymentPage.fillCVVField(getRandomValidCVV());
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotification();
        assertNull(SQLRequests.getStatusByCard());
    }
         
    @Test
    void shouldCheckInvalidMonthCard() { //проверка неверного значения по месяцу карты
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(invalidMonth);
        paymentPage.fillYearField(DataHelper.getYear());
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvcField(getRandomValidCVV());
        paymentPage.clickContinueButton();
        $(byText ("Неверный формат")).shouldBe(visible, Duration.ofSeconds(30));
    }
    @Test
    void shouldCheckInvalidYearCard() { //проверка неверного значения по году действия карты
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(invalidYear);
        paymentPage.fillHolderField(DataHepler.getRandomValidName());
        paymentPage.fillCvcField(getRandomValidCVV());
        paymentPage.clickContinueButton();
        $(byText ("Неверный формат")).shouldBe(visible, Duration.ofSeconds(30));
    }
       @Test
    void shouldCheckPastYearByCard() {  //проверка срока действия карты, прошлого года
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(pastYear);
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvcField(getRandomValidCVV());
        paymentPage.clickContinueButton();
        $(byText ("Истёк срок действия карты")).shouldBe(visible, Duration.ofSeconds(30));

    }
        @Test
    void shouldCheckFalseMonthCard() {  //проверка  неверно заполненного поля "месяц"
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(falseMonth);
        paymentPage.fillYearField(DataHelper.getYear(0));
        paymentPage.fillHolderField(DataHelper.getRandomValidName());
        paymentPage.fillCvcField(getRandomValidCVV());
        paymentPage.clickContinueButton();
        $(byText ("Неверно указан срок действия карты")).shouldBe(visible, Duration.ofSeconds(30));
    }
     @Test
    void shouldCheckInvalidHolderCard() {  //проверка неверно заполненного поля "владелец"
        paymentPage.openCardPaymentPage();
        paymentPage.fillCardNumberField(approvedCardNumber);
        paymentPage.fillMonthField(DataHelper.getMonth());
        paymentPage.fillYearField(DataHelper.getYear(0));
        paymentPage.fillHolderField(invalidName);
        paymentPage.fillCvcField(getRandomValidCVV());
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
        paymentPage.fillCVVField(invalidCVV);
        paymentPage.clickContinueButton();
        paymentPage.shouldHaveErrorNotificationWrongFormat();
        assertNull(SQLRequests.getStatusByCard());
    }

    private void stayAllFieldsEmpty() {  //оставить все поля пустыми
        paymentPage.fillCardNumberField(emptyCard);
        paymentPage.fillMonthField(emptyMonth);
        paymentPage.fillYearField(emptyYear);
        paymentPage.fillHolderField(emptyName);
        paymentPage.fillCvcField(emptyCVV);
        paymentPage.clickContinueButton();
    }
}
