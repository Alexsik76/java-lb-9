import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final int NUM_ACCOUNTS = 100;
    private static final int NUM_THREADS = 10000;

    public static void main(String[] args) throws InterruptedException {
        // Створюємо рахунки та вносимо випадкові суми
        List<Account> accounts = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < NUM_ACCOUNTS; i++) {
            accounts.add(new Account(random.nextInt(10000)));
        }

        // Підраховуємо загальну суму грошей до переказів
        int totalBefore = getTotalBalance(accounts);

        // Створюємо банк
        Bank bank = new Bank();

        // Використовуємо try-with-resources для ExecutorService
        try (ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS)) {
            // Запускаємо перекази в потоках
            for (int i = 0; i < NUM_THREADS; i++) {
                executor.execute(() -> {
                    int fromIndex = random.nextInt(NUM_ACCOUNTS);
                    int toIndex = random.nextInt(NUM_ACCOUNTS);
                    while (fromIndex == toIndex) {
                        toIndex = random.nextInt(NUM_ACCOUNTS);
                    }
                    int amount = random.nextInt(100);
                    bank.transfer(accounts.get(fromIndex), accounts.get(toIndex), amount);
                });
            }

            // Чекаємо завершення переказів
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);
        }

        // Підраховуємо загальну суму грошей після переказів
        int totalAfter = getTotalBalance(accounts);

        // Виводимо результати
        System.out.println("Загальна сума до переказів: " + totalBefore);
        System.out.println("Загальна сума після переказів: " + totalAfter);
    }

    private static int getTotalBalance(List<Account> accounts) {
        return accounts.stream().mapToInt(Account::getBalance).sum();
    }
    }
