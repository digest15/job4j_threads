package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Runnable printThreadName = () -> System.out.println(Thread.currentThread().getName());
        var first = new Thread(printThreadName, "first");
        var second = new Thread(printThreadName, "second");
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED
                || second.getState() != Thread.State.TERMINATED) {
            System.out.println(first.getName() + " : " + first.getState());
            System.out.println(second.getName() + " : " + second.getState());
        }
        System.out.println("All threads finish his work");
    }
}
