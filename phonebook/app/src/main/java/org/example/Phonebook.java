package app.src.main.java.org.example;

import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Phonebook {
    private static final int INITIAL_SIZE = 100_000;

    private HashMap<Integer, PhonebookRecord> _records = new HashMap<Integer, PhonebookRecord>();
    private PhonebookLogger updateLogger = new PhonebookLogger();
    private AtomicBoolean running;

    /**
     * Конструктор класса Phonebook.
     * Инициализирует телефонную книгу с заданным количеством случайных записей.
     */
    public Phonebook() {
        running = new AtomicBoolean(true);
    }


    public void initializeRandom() {
        for (int i = 0; i < INITIAL_SIZE; i++) {
            //_records.put(i, new PhonebookRecord(i, RandomDataGenerator.getRandomName(), RandomDataGenerator.getRandomPhone()));
            add(RandomDataGenerator.getRandomName(), RandomDataGenerator.getRandomPhone());
        }
    }

    public PhonebookRecord add(String name, String phone) {
        PhonebookRecord record = new PhonebookRecord(_records.size(), name, phone);
        _records.put(_records.size(), record);
        return record;
    }

    /**
     * Запускает процесс периодического обновления записей в отдельном потоке.
     * Обновления выполняются до тех пор, пока флаг running установлен в true.
     */
    public void runUpdates() {
        Thread updateThread = new Thread(() -> {
            while (running.get()) {
                int N = RandomDataGenerator.getRandomInt(500, 5000); // 500-5000 мс
                modifyRecords();
                try {
                    Thread.sleep(N);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        updateThread.start();
    }

    /**
     * Модифицирует случайное количество записей в телефонной книге.
     * Изменяет номера телефонов и логирует изменения.
     */
    public void modifyRecords() {
        System.out.println(String.format("[%s] start modifying...", Instant.now()));
        int randomId;
        PhonebookRecord record;
        int recordsToUpdate = RandomDataGenerator.getRandomInt(1, _records.size());
        //System.out.println("records to update: " + recordsToUpdate);
        for (int i = 0; i < recordsToUpdate; i++) {
            randomId = RandomDataGenerator.getRandomInt(0, INITIAL_SIZE);
            //System.out.println("random id: " + randomId);
            record = _records.get(randomId);
            String oldPhone = record.setPhone(RandomDataGenerator.getRandomPhone());
            updateLogger.add(record.getLastModified(), randomId, record.getPhone(), oldPhone);
        }
        System.out.println(String.format("[%s] %d records modified", Instant.now(), recordsToUpdate));
    }

    /**
     * Выводит все записи телефонной книги с учетом изменений до указанной временной метки.
     *
     * @param timestamp - временная метка, до которой нужно показать состояние записей
     */
    public void printRecords(Long timestamp) {
        HashMap<Integer, PhonebookUpdateRecord> rollbackUpdates = updateLogger.getUpdates(timestamp);

        PhonebookUpdateRecord logRecord;
        Long updateTime;
        PhonebookRecord record;
        for (int i = 0; i < _records.size(); i++) {
            record = _records.get(i);
            if (rollbackUpdates.containsKey(record.getID())) {
                System.out.println(new PhonebookRecord(-1,record.getName(), rollbackUpdates.get(record.getID()).old()).ToString());
            } else {
                System.out.println(record.ToString());
            }
        }
    }

    /**
     * Останавливает процесс обновления записей.
     */
    public void stopUpdates() {
        running.set(false);
    }

    public int size() {
        return _records.size();
    }

}