package data;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

import static java.lang.String.valueOf;

public class DataHelper {

    static String [] approvedCardNumber = {"4444 4444 4444 4441"};
    static String [] declinedCardNumber = {"4444 4444 4444 4442"};
    static String [] name = {"Ivan","Nastiia","Alex", "Kate", "Anna"};
    static String [] surname = {"Ivanov","Vasileva","Ionov", "Avvvvvvv", "Yagodina"};
    static String [] invalidName = {"Антон", "!@#$"};
    static String [] invalidSurname = {"Антонов", "12345"};

    static String [] randomCardNumber = {
            "4444 4444 4444 1234",
            "4444 4444 4444 0000",
            "0000 0000 0000 0000",
            "1111 1111 1111 1111",
            "1234 5678 9011 1234"
    };
    static String [] randomShortCardNumber = {
            "4444 4444 4444 123",
            "4444 4444 4444 000",
            "0000 0000 0000 000",
            "1111 1111 1111 111",
            "1234 5678 9011 123"
    };
    static String [] randomLongCardNumber = {
            "4444 4444 4444 12043",
            "4444 4444 4444 00044",
            "0000 0000 0000 00000",
            "1111 1111 1111 11111",
            "1234 5678 9011 12333"
    };
    static String[] symbols = {"#","&","@","$","&","+","*","^","%","№","="};
    static String[] alphabetEn = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static String emptySymbol() {
        return "";
    }

    //Номера карт
    public static String approvedCardNumber() {
        Random random = new Random();
        return approvedCardNumber[random.nextInt(approvedCardNumber.length)];
    }

    public static String declinedCardNumber() {
        Random random = new Random();
        return declinedCardNumber[random.nextInt(declinedCardNumber.length)];
    }

    public static String randomDigit() { //цифра
        Random random = new Random();
        return valueOf(random.nextInt(9));
    }

    public static String randomCardNumber() {
        Random random = new Random();
        return randomCardNumber[random.nextInt(randomCardNumber.length)];
    }

    public static String shortCardNumber() {
        Random random = new Random();
        return randomShortCardNumber[random.nextInt(randomShortCardNumber.length)];
    }

    public static String longCardNumber() {
        Random random = new Random();
        return randomLongCardNumber[random.nextInt(randomLongCardNumber.length)];
    }


    // Владелец карты
    public static String cardHolder() {
        Random random = new Random();
        return (name[random.nextInt(name.length)] + " " + surname[random.nextInt(surname.length)]);
    }

    public static String numbersCardHolder() {
        Random random = new Random();

        String numOne = valueOf(random.nextInt(9999999));
        String numTwo = valueOf(random.nextInt(9999999));
        return numOne + " " + numTwo;
    }

    public static String negativeCardHolder() {
        Random random = new Random();
        return (invalidName[random.nextInt(invalidName.length)] + " " + invalidSurname[random.nextInt(invalidSurname.length)]);
    }

    public static String onlyNameCardHolder() {
        Random random = new Random();
        return name[random.nextInt(name.length)];
    }


    //Тестовые данные МЕСЯЦ
    public static String month() {
        Random random = new Random();
        return LocalDate.now().plusMonths(random.nextInt(12)).format(DateTimeFormatter.ofPattern("MM"));
    }
    public static String thisMonth() {
        return LocalDate.now().plusMonths(0).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String lastMonth() {
        return LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("MM"));
    }
    public static String zeroMonth() {
        return "00";
    }

    public static String thirteenMonth() {
        return "13";
    }


    //Тестовые данные ГОД
    public static String year() {
        Random random = new Random();
        return LocalDate.now().plusYears(random.nextInt(5)).format(DateTimeFormatter.ofPattern("yy"));
    }
    public static String thisYear() {
        return LocalDate.now().minusYears(0).format(DateTimeFormatter.ofPattern("yy"));
    }
    public static String pastYear() {
        return LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String moreFiveYear() {
        return LocalDate.now().plusYears(6).format(DateTimeFormatter.ofPattern("yy"));
    }


    //Тестовые данные CVV

    public static String cvv() {
        Random random = new Random();
        return valueOf(random.nextInt(900) + 100);
    }

    public static String zeroCvv() {
        return "000";
    }
    public static String oneOrTwoSymbolsCvv() {
        Random random = new Random();
        return valueOf(random.nextInt(99));
    }

    public static String lettersOnCVV() {
        int count = 3;
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < count; x++) {
            sb.append(alphabetEn[rand.nextInt(alphabetEn.length)]);
        }
        return sb.toString();
    }

    public static String symbolCVV() {
        int count = 3;
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < count; x++) {
            sb.append(symbols[rand.nextInt(symbols.length)]);
        }
        return sb.toString();
    }
}