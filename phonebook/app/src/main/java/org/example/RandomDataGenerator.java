package app.src.main.java.org.example;

import java.util.Locale;
import java.util.Random;
import com.github.javafaker.Faker;

/**
 * Класс RandomDataGenerator предназначен для генерации рандомных значений для телефонной книги
 */
public class RandomDataGenerator {
    private static final Random RANDOM = new Random();
    private static final Faker faker = new Faker();

    public static String getRandomPhone() {
        return faker.phoneNumber().phoneNumber();
    }
    public static String getRandomName() {
        return faker.name().fullName();
    }

    public static Integer getRandomInt(int first, int last) {
        return RANDOM.nextInt(last - first) + first;
    }
}