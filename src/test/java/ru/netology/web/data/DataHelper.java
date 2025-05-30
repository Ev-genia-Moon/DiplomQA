package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private static Faker faker = new Faker(new Locale("en"));
    private static Faker fakerRu = new Faker(new Locale("ru"));

    private DataHelper() {
    }

    public static CardInfo getCardAPPROVED() {
        return new CardInfo("4444 4444 4444 4441", "APPROVED");
    }

    public static CardInfo getCardDECLINED() {
        return new CardInfo("4444 4444 4444 4442", "DECLINED");
    }

    public static String getRandomMonth(int month) {
        return LocalDate.now().plusMonths(month).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getRandomYear(int year) {
        return LocalDate.now().plusYears(year).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getRandomName() {
        return faker.name().fullName();
    }

    public static String getEmptyField() {
        return "";
    }

    public static String getRandomCardNumberLess16() {
        int shortNumber = faker.random().nextInt(16);
        return faker.number().digits(shortNumber);
    }

    public static String getRandomCardNumberAllNulls() {
        String cardNumber;
        return cardNumber = "0000 0000 0000 0000";
    }

    public static String getInvalidMonthNull() {
        return "00";
    }

    public static String getInvalidMonthOneNumber(String numberMonth) {
        return numberMonth;
    }

    public static String getInvalidYearOneNumber(String numberYear) {
        return numberYear;
    }

    public static String getRandomNameRU() {
        return fakerRu.name().fullName();
    }

    public static String getInvalidNumberName() {
        return faker.number().digit();
    }

    public static String getInvalidSpecialCharactersName() {
        return "&^^%&^%*&^";
    }

    public static String getNumberCVC(int code) {
        return faker.number().digits(code);
    }


    @Value
    public static class CardInfo {
        public String cardNumber;
        public String Status;
    }


}
