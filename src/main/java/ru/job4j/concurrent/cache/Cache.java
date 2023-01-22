package ru.job4j.concurrent.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.getId(), model) == null;
    }

    public boolean update(Base model) throws OptimisticException {
        return memory.computeIfPresent(model.getId(), (k, v) -> {
                    if (v.getVersion() != model.getVersion()) {
                        throw new OptimisticException("Versions aren't equal");
                    }
                    return model;
                }
        ) != null;
    }

    public void delete(Base model) {
        memory.remove(model.getId(), model);
    }
}
