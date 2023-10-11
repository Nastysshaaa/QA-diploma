package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLRequests;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.PaymentPage;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestPurchaseCard {
    private PaymentPage paymentPage;

    @BeforeEach
    void setUp() {
        paymentPage = new PaymentPage();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    static void sqlClean() throws SQLException {
        SQLRequests.clear();
    }

    @Test
    @DisplayName("Покупка активной картой")
    public void successfulPayApprovedCardPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.thisMonth(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.success();
    }


    @Test
    @DisplayName("Покупка  с заблокированной картой")
    public void unsuccessfulPayDeclinedCardPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.declinedCardNumber(), DataHelper.thisMonth(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.error();
    }


    @Test
    @DisplayName("Покупка тура со случайной карты")
    public void randomCardPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.randomCardNumber(), DataHelper.month(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.error();
    }


    @Test
    @DisplayName("Заполнение поле \"Номер карты\" одной цифрой")
    public void oneNumberCard() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.randomDigit(), DataHelper.month(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.invalidFormat();
    }

    @Test
    @DisplayName("Заполнение поле \"Номер карты\" пятнадцатью цифрами")
    public void shortNumberPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.shortCardNumber(), DataHelper.month(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.invalidFormat();
    }

    @Test
    @DisplayName("Заполнение поле \"Номер карты\" семнадцатью цифрами")
    public void longNumberPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.longCardNumber(), DataHelper.month(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.error();
    }


    @Test
    @DisplayName("Пустой номер карты")
    public void emptyNumberCard() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.emptySymbol(), DataHelper.month(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.invalidFormat();
    }

    @Test
    @DisplayName("Поле \"Месяц\" заполнено значением предыдущего месяца этого года")
    public void lastMonth() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.lastMonth(), DataHelper.thisYear(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.invalidDate();
    }

    @Test
    @DisplayName("Поле \"Месяц\" заполнено \"00\"")
    public void zeroMonth() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.zeroMonth(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.invalidFormat();
    }


    @Test
    @DisplayName("Поле \"Месяц\" заполнено \"13\"")
    public void thirteenMonth() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.thirteenMonth(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.invalidDate();
    }

    @Test
    @DisplayName("Поле \"Месяц\" заполнено одной цифрой")
    public void oneNumberMonth() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.randomDigit(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.invalidFormat();
    }

    @Test
    @DisplayName("Поле \"Месяц\" пустое")
    public void emptyMonthPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.emptySymbol(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.invalidFormat();
    }

    @Test
    @DisplayName("Поле \"Год\" заполнено \"00\"")
    public void shouldErrorZeroYearPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.month(), DataHelper.zeroMonth(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.expiredCard();
    }


    @Test
    @DisplayName("Указан прошлый год")
    public void lastYearPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.month(), DataHelper.pastYear(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.expiredCard();
    }

    @Test
    @DisplayName("Указан срок действия больше 5 лет")
    public void moreSixYearPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.month(), DataHelper.moreFiveYear(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.invalidDate();
    }


    @Test
    @DisplayName("Один цифра в поле \"Год\"")
    public void oneDigitYearPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.month(), DataHelper.randomDigit(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.invalidFormat();
    }


    @Test
    @DisplayName("Пустое поле  \"Год\"")
    public void emptyYearPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.month(), DataHelper.emptySymbol(), DataHelper.cardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.invalidFormat();
    }

    @Test
    @DisplayName("Заполнено только имя в поле \"Владелец карты\"")
    public void onlyNamePay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.month(), DataHelper.year(), DataHelper.onlyNameCardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.invalidFormat();
    }


    @Test
    @DisplayName("Заполнено поле \"Владелец карты\" некорректными данными")
    public void negativeCardHolderPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.month(), DataHelper.year(), DataHelper.negativeCardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.error();
    }


    @Test
    @DisplayName("Заполнено поле \"Владелец карты\" цифрами")
    public void numberCardHolderPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.month(), DataHelper.year(), DataHelper.numbersCardHolder(), DataHelper.cvv());
        PaymentPage.buttonContinue();
        PaymentPage.error();
    }


    @Test
    @DisplayName("Буквы в поле \"CVV\"")
    public void letterCvvPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.month(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.lettersOnCVV());
        PaymentPage.buttonContinue();
        PaymentPage.invalidFormat();
    }


    @Test
    @DisplayName("Спецсимволы в поле \"CVV\"")
    public void symbolsCvvPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.month(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.symbolCVV());
        PaymentPage.buttonContinue();
        PaymentPage.invalidFormat();
    }

    @Test
    @DisplayName("Пусто в поле \"CVV\"")
    public void emptyCvvPay() {
        PaymentPage.payCard();
        PaymentPage.Card(DataHelper.approvedCardNumber(), DataHelper.month(), DataHelper.year(), DataHelper.cardHolder(), DataHelper.emptySymbol());
        PaymentPage.buttonContinue();
        PaymentPage.required();
    }
}