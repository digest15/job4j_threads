package ru.job4j.concurrent.account;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {
    @GuardedBy("accounts")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public boolean add(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account mustn't be null");
        }
        synchronized (accounts) {
            accounts.put(account.id(), account);
            return true;
        }
    }

    public boolean update(Account account) {
        synchronized (accounts) {
            return accounts.replace(account.id(), account) != null;
        }
    }

    public boolean delete(int id) {
        synchronized (accounts) {
            return accounts.remove(id) != null;
        }
    }

    public Optional<Account> getById(int id) {
        synchronized (accounts) {
            return Optional.ofNullable(accounts.get(id));
        }
    }

    public boolean transfer(int fromId, int toId, int amount) {
        synchronized (accounts) {
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
}
