package data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {

    public static String approvedCardNumber = "4444 4444 4444 4441";
    public static String declinedCardNumber = "4444 4444 4444 4442";
    public static String randomCardNumber = "4444 4444 4444 1234";
    public static String emptyCardNumber = " ";
    public static String zeroCardNumber = "0000 0000 0000 0000";
    public static String shortCardNumber = "4444";
    public static String invalidMonth = "95";
    public static String zeroMonth = "00";
    public static String emptyMonth = " ";
    public static String invalidYear = "2O";
    public static String lastYear = "21";
    public static String emptyYear = "";
    public static String zeroYear = "00";
    public static String invalidHolder = "Алекс Александров";
    public static String emptyName = " ";
    public static String zeroName = "000000000";
    public static String invalidCvv = "1ss";
    public static String emptyCvv = " ";

    public static String getRandomCard() {
        Faker faker = new Faker();
        return faker.finance().creditCard();
    }

    public static String getMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getYear(int years) {
        return LocalDate.now().plusYears(years).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getRandomValidName() {
        Faker faker = new Faker(new Locale("en"));
        String name = faker.name().firstName();
        String surname = faker.name().lastName();
        return name + " " + surname;
    }

    public static String getRandomValidCvv() {
        Faker faker = new Faker();
        return faker.number().digits(3);
    }
}
