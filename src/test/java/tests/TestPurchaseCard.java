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
        
        
}
