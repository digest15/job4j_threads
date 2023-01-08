package ru.job4j.concurrent.account;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {
    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account mustn't be null");
        }
        return accounts.putIfAbsent(account.id(), account) != null;
    }

    public synchronized boolean update(Account account) {
        return accounts.replace(account.id(), account) != null;
    }

    public synchronized boolean delete(int id) {
        return accounts.remove(id) != null;
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        var isOk = false;
        Optional<Account> from = getById(fromId)
                .filter(a -> a.amount() >= amount);
        if (from.isPresent()) {
            Optional<Account> to = getById(toId);
            if (to.isPresent()) {
                accounts.put(fromId, new Account(fromId, from.get().amount() - amount));
                accounts.put(toId, new Account(toId, to.get().amount() + amount));
                isOk = true;
            }
        }
        return isOk;
    }
}
