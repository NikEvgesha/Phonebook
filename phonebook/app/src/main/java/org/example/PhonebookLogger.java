package app.src.main.java.org.example;

import java.time.Instant;
import java.util.ArrayList;
import java.util.*;

/**
 * Запись, представляющая изменение в телефонной книге.
 *
 * @param timestamp временная метка изменения
 * @param modified  новый номер телефона
 * @param old       старый номер телефона
 */
record PhonebookUpdateRecord(Long timestamp, String modified, String old) {}

/**
 * Класс PhonebookLogger предназначен для логирования изменений в записях телефонной книги.
 * Хранит историю изменений номеров телефонов для каждого идентификатора.
 */
public class PhonebookLogger {
    // Хранилище логов: ключ - ID записи, значение - список изменений для этого ID
    HashMap<Integer, ArrayList<PhonebookUpdateRecord>> _log = new HashMap<>();

    /**
     * Добавляет запись об изменении номера телефона в лог.
     *
     * @param timestamp     временная метка изменения
     * @param id            идентификатор записи в телефонной книге
     * @param modifiedPhone новый номер телефона
     * @param oldPhone      старый номер телефона
     */
    public void add(Long timestamp, Integer id, String modifiedPhone, String oldPhone) {
        if (!_log.containsKey(id)) {
            _log.put(id, new ArrayList<PhonebookUpdateRecord>());
        }
        _log.get(id).add(new PhonebookUpdateRecord(timestamp, modifiedPhone, oldPhone));
        //System.out.println(String.format("[%s] id %d: %s -> %s", Instant.ofEpochMilli(timestamp), id, oldPhone, modifiedPhone));
    }

    /**
     * Возвращает последние изменения для записей, произошедшие после указанной временной метки.
     *
     * @param timestamp временная метка, до которой нужно исключить изменения
     * @return HashMap, где ключ - ID записи, значение - последнее изменение после указанной метки
     */
    public HashMap<Integer, PhonebookUpdateRecord> getUpdates(Long timestamp) {
        HashMap<Integer, PhonebookUpdateRecord> lastUpdates = new HashMap<>();

        for (Map.Entry<Integer, ArrayList<PhonebookUpdateRecord>> entry : _log.entrySet()) {
            Integer id = entry.getKey();
            ArrayList<PhonebookUpdateRecord> updates = entry.getValue();

            for (PhonebookUpdateRecord record : updates) {
                if (record.timestamp() > timestamp) {
                    lastUpdates.put(id, record);
                    break;
                }
            }
        }

        return lastUpdates;
    }

    /**
     * Выводит весь лог изменений в консоль.
     * Для каждого ID выводятся все изменения номеров телефонов.
     */
    public void printLog() {
        for (var updates : _log.entrySet()) {
            for (var rec : updates.getValue()) {
                System.out.println(String.format("[%s] id %d: %s -> %s", rec.timestamp(), updates.getKey(), rec.old(), rec.modified()));
            }
        }
    }

}
