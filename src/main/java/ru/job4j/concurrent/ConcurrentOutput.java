package ru.job4j.concurrent;

public class ConcurrentOutput {
    public static void main(String[] args) {
        Runnable print = () -> System.out.println(Thread.currentThread().getName());
        Thread another = new Thread(print);
        Thread second = new Thread(print);
        another.start();
        second.start();
        System.out.println(Thread.currentThread().getName());
    }
}
