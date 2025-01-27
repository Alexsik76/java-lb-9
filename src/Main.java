
public class Main {
    public static void main(String[] args) throws InterruptedException {
        int NUM_ACCOUNTS = 100;
        final int NUM_THREADS = 10000;

        System.out.println("Тестування банку");
        BankTest bankTest = new BankTest(NUM_ACCOUNTS, NUM_THREADS);
        bankTest.execute();
        System.out.printf("Проведено %d транзакцій по %d рахункам", NUM_THREADS, NUM_ACCOUNTS);
        System.out.println("\n\nЗавдання№2 (тестування буфера)");
        ProducerConsumerTask task = new ProducerConsumerTask();

    }
    }
