package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Runnable printThreadName = () -> System.out.println(Thread.currentThread().getName());
        var first = new Thread(printThreadName);
        var second = new Thread(printThreadName);
        first.start();
        second.start();
        var isDone = true;
        while (isDone) {
            isDone = !(Thread.State.TERMINATED.equals(first.getState())
                    && Thread.State.TERMINATED.equals(second.getState()));
        }
        System.out.println("All threads finish his work");
    }
}
