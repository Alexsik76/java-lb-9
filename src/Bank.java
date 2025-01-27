import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {
    private static List<Account> accounts = new ArrayList<>();
    Random random;

    public Bank(int NUM_ACCOUNTS) {
        random = new Random();
        for (int i = 0; i < NUM_ACCOUNTS; i++) {
            accounts.add(new Account(random.nextInt(10000)));
        }
    }

    public List<Account> getAccounts() {
        return accounts;
    }



    public void transfer(Account from, Account to, int amount) {
        // Отримуємо блокування для обох рахунків в певному порядку
        Lock firstLock = from.getLock();
        Lock secondLock = to.getLock();
        if (firstLock.hashCode() > secondLock.hashCode()) {
            firstLock = to.getLock();
            secondLock = from.getLock();
        }

        // Блокуємо рахунки в визначеному порядку
        firstLock.lock();
        try {
            secondLock.lock();
            try {
                // Виконуємо переказ
                if (from.getBalance() >= amount) {
                    from.withdraw(amount);
                    to.deposit(amount);
                }
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }

    }
}
