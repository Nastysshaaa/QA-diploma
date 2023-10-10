package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class PaymentPage {
    static List<SelenideElement> input = $$(".input__control");
    static SelenideElement cardNumber = input.get(0);
    static SelenideElement month = input.get(1);
    static SelenideElement year = input.get(2);
    static SelenideElement cardOwner = input.get(3);
    static SelenideElement cvv = input.get(4);
    public static void payCard() {
        open("http://localhost:8080");
        $$(".button__content").find(exactText("Купить")).click();
        $$(".heading_theme_alfa-on-white").find(exactText("Оплата по карте")).shouldBe(visible);
    }

    public static void buyCredit() {
        open("http://localhost:8080");
        $$(".button__content").find(exactText("Купить в кредит")).click();
        $$(".heading_theme_alfa-on-white").find(exactText("Кредит по данным карты")).shouldBe(visible);
    }

    public static void success() {
        $$(".notification__title").find(exactText("Успешно")).shouldHave(visible, Duration.ofSeconds(15));
    }

    public static void error() {
        $$(".notification__title").find(exactText("Ошибка")).shouldHave(visible, Duration.ofSeconds(15));
    }

    public static void invalidFormat() {
        $$(".input__sub").find(exactText("Неверный формат")).shouldBe(visible);
    }

    public static void invalidDate() {
        $$(".input__sub").find(exactText("Неверно указан срок действия карты")).shouldBe(visible);
    }

    public static void expiredCard() {
        $$(".input__sub").find(exactText("Истёк срок действия карты")).shouldBe(visible);
    }

    public static void required() {
        $$(".input__sub").find(exactText("Поле обязательно для заполнения")).shouldBe(visible);
    }


    public static void Card(String number, String cardMonth, String cardYear, String owner, String CVV) {
        cardNumber.setValue(number);
        month.setValue(cardMonth);
        year.setValue(cardYear);
        cardOwner.setValue(owner);
        cvv.setValue(CVV);
    }

    public static void buttonContinue() {
        $$(".button__content").find(exactText("Продолжить")).click();
    }

}