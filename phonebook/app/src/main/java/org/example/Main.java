package app.src.main.java.org.example;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    /**
     * Точка входа в программу.
     * Создает телефонную книгу, запускает обновления, ожидает заданное время,
     * затем выводит записи до случайной временной метки и останавливает обновления.
     */
    public static void main(String[] args) {
        Phonebook phonebook = new Phonebook();
        phonebook.initializeRandom();
        phonebook.runUpdates();
        // Случайная задержка от 60 до 300 секунд
        int T = RandomDataGenerator.getRandomInt(60, 300);
        System.out.println(String.format("print delay (T) = %d sec.", T));
        try {
            Thread.sleep(T* 1000);
            Long now = Instant.now().toEpochMilli();
            // Случайное смещение времени назад от 0 до 30 секунд
            int M = RandomDataGenerator.getRandomInt(0, 30) * 1000;
            Long timestamp = now - M;
            System.out.println(String.format("===Printing data before %s===", Instant.ofEpochMilli(timestamp)));
            phonebook.printRecords(timestamp);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            phonebook.stopUpdates();
        }
    }
}

