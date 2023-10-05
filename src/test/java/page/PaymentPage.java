package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class PaymentPage {
    private final SelenideElement buyButton = $(withText("Купить"));
    private final SelenideElement buyByCredit = $(withText("Купить в кредит"));
    private final SelenideElement cardNumberField = $("[placeholder=\"0000 0000 0000 0000\"]");
    private final SelenideElement monthField = $("[placeholder=\"08\"]");
    private final SelenideElement yearField = $("[placeholder=\"22\"]");
    private final SelenideElement holderField = $(byText("Владелец")).parent().$(".input__control");
    private final SelenideElement cvcField = $("[placeholder=\"999\"]");
    private final SelenideElement continueButton = $(withText("Продолжить"));

    private final SelenideElement notification = $("div.notification__visible  div.notification__content");
    private final SelenideElement wrongFormat = $(withText("Неверный формат"));
    private final SelenideElement invalidCard = $(withText("Неверно указан срок действия карты"));
    private final SelenideElement cardExpired = $(withText("Истёк срок действия карты"));
    private final SelenideElement requiredField = $(withText("Поле обязательно для заполнения"));
    private final SelenideElement subField = $("input__sub");
    public void openCardPaymentPage(){
        buyButton.click();
    }
    public void openCreditCardPaymentPage(){
        buyByCredit.click();
    }
    public void fillCardNumberField(String cardNumber){
        cardNumberField.setValue(cardNumber);

    }
    public void fillMonthField(String month){
        monthField.setValue(month);
    }
    public void fillYearField(String year) {
        yearField.setValue(year);
    }

    public void fillHolderField(String holder) {
        holderField.setValue(holder);
    }

    public void fillCvcField(String cvc) {
        cvcField.setValue(cvc);
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public void shouldHaveSuccessNotification() {
        notification.shouldHave(Condition.text("Операция одобрена Банком."), Duration.ofSeconds(15));
    }

    public void shouldHaveErrorNotification() {
        notification.shouldHave(Condition.text("Ошибка! Банк отказал в проведении операции"), Duration.ofSeconds(15));
    }

    public void shouldHaveErrorNotificationWrongFormat() {
        wrongFormat.shouldHave(Condition.text("Неверный формат"));
    }

    public void shouldHaveErrorNotificationInvalidCard() {
        invalidCard.shouldHave(Condition.text("Неверно указан срок действия карты"));
    }

    public void shouldHaveErrorNotificationCardExpired() {
        cardExpired.shouldHave(Condition.text("Истёк срок действия карты"));
    }

    public void shouldHaveErrorNotificationRequiredField() {
        requiredField.shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

}