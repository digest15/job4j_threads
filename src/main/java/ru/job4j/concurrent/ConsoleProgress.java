package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    private static final long PAUSE = 500;

    private final char[] process = new char[]{'-', '\\', '|', '/'};

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(1000);
        progress.interrupt();
    }

    @Override
    public void run() {
        byte i = 0;
        while (!Thread.currentThread().isInterrupted()) {
            System.out.printf("\rLoading...%c", process[i]);
            if (++i == 4) {
                i = 0;
            }
            try {
                Thread.sleep(PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
