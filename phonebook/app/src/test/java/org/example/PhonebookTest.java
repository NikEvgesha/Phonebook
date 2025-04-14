package app.src.main.java.org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class PhonebookTest {

    private Phonebook phonebook;

    @BeforeEach
    public void setUp() {
        phonebook = new Phonebook();
    }

    @Test
    public void testPhonebookRandomInitialization() {
        phonebook.initializeRandom();
        assertEquals(100_000, phonebook.size(), "Phonebook should contain 100,000 records after initialization");
        HashMap<Integer, PhonebookRecord> records = getRecords();
        assertTrue(records.containsKey(0), "Record with ID 0 should exist");
        assertTrue(records.containsKey(99_999), "Record with ID 99,999 should exist");
    }


    @Test
    public void testPhonebookRecordCreation() {
        PhonebookRecord record = phonebook.add("John Doe", "+1234567890");
        assertEquals(1, phonebook.size(), "Phonebook should contain 1 record");
        assertEquals(0, record.getID(), "First record should have ID 0");
        assertEquals("John Doe", record.getName(), "Name should match");
        assertEquals("+1234567890", record.getPhone(), "Phone should match");
    }

    @Test
    public void testPhonebookRecordUpdate() {
        // Тест обновления телефона
        PhonebookRecord record = phonebook.add("John Doe", "+1234567890");
        String oldPhone = record.setPhone("+1111111111");
        assertEquals("+1234567890", oldPhone, "Old phone should be returned");
        assertEquals("+1111111111", record.getPhone(), "New phone should be set");
    }

    @Test
    public void testModifyRecords() throws InterruptedException {
        // Тест модификации записей
        phonebook.initializeRandom();
        phonebook.modifyRecords();
        HashMap<Integer, PhonebookRecord> records = getRecords();
        boolean modified = false;
        for (PhonebookRecord record : records.values()) {
            if (record.getLastModified() > System.currentTimeMillis() - 1000) {
                modified = true;
                break;
            }
        }
        assertTrue(modified, "At least one record should be modified");
    }


    @Test
    public void testPhonebookLoggerAddAndGetUpdates() {
        // Тест логирования изменений
        PhonebookLogger logger = new PhonebookLogger();
        long timestamp = System.currentTimeMillis();
        logger.add(timestamp, 1, "+123", "+456");
        logger.add(timestamp + 1000, 1, "+789", "+123");

        HashMap<Integer, PhonebookUpdateRecord> updates = logger.getUpdates(timestamp + 500);
        assertEquals(1, updates.size(), "Should return one update after timestamp");
        assertEquals("+789", updates.get(1).modified(), "Latest modified phone should match");
        assertEquals("+123", updates.get(1).old(), "Old phone should match");
    }

    @Test
    public void testPrintRecordsBeforeTimestamp() throws InterruptedException {
        PhonebookRecord record = phonebook.add("Test User", "+000");
        phonebook.runUpdates();
        long timeBefore = System.currentTimeMillis();
        try {
            Thread.sleep(5000); // ждем, чтобы точно прошло обновление

            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));
            phonebook.printRecords(timeBefore);
            String output = outContent.toString();
            assertTrue(output.contains("Test User\t+000"), "Should print original phone before update");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        phonebook.stopUpdates();

    }



    // Вспомогательный метод для доступа к приватному полю _records
    private HashMap<Integer, PhonebookRecord> getRecords() {
        try {
            java.lang.reflect.Field field = Phonebook.class.getDeclaredField("_records");
            field.setAccessible(true);
            return (HashMap<Integer, PhonebookRecord>) field.get(phonebook);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access _records field", e);
        }
    }
}
