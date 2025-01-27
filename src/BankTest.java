import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BankTest {

    int NUM_ACCOUNTS;
    int NUM_THREADS;
    Bank bank;
    private static List<Account> accounts;
    Random random = new Random();

    public BankTest(int NUM_ACCOUNTS, int NUM_THREADS) throws InterruptedException {
        bank = new Bank(NUM_ACCOUNTS);
        this.NUM_ACCOUNTS = NUM_ACCOUNTS;
        this.NUM_THREADS = NUM_THREADS;
        accounts = bank.getAccounts();

    }
    public void execute() throws InterruptedException {
            int totalBefore = getTotalBalance(accounts);
            System.out.println("Перед транзакціями:");
            print5Accounts();



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
            System.out.println("Після транзакцій:");
            print5Accounts();
            // Виводимо результати
            System.out.println("Загальна сума до переказів: " + totalBefore);
            System.out.println("Загальна сума після переказів: " + totalAfter);
        }

       // Підраховуємо загальну суму грошей до переказів

    private static int getTotalBalance(List<Account> accounts) {
        return accounts.stream().mapToInt(Account::getBalance).sum();
    }
    public void print5Accounts() {
        int[] test_balances;
        test_balances = accounts
                .subList(0,5)
                .stream()
                .mapToInt(Account::getBalance)
                .toArray();
        System.out.println(Arrays.toString(test_balances));
    }
}