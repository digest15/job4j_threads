package ru.job4j.concurrent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class Wget implements Runnable {
    private final String url;

    private final String file;

    private final long goalTime;

    private static final int BATCH = 1024;

    public Wget(String url, int speed, String file) {
        this.url = url;
        this.goalTime = speed <= 0 ? 0 : BATCH / speed;
        this.file = file;
    }

    public static void main(String[] args) throws InterruptedException {
        validate(args);
        var url = args[0];
        var speed = Integer.parseInt(args[1]);
        var file = args[2];
        Thread wget = new Thread(new Wget(url, speed, file));
        wget.start();
        wget.join();
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte[] dataBuffer = new byte[BATCH];
            int bytesRead = 0;
            while (!Thread.currentThread().isInterrupted() && (bytesRead) != -1) {

                long start = System.currentTimeMillis();
                bytesRead = in.read(dataBuffer, 0, BATCH);
                long end = System.currentTimeMillis();

                if (bytesRead != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                    controlSpeed(end - start);
                }
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private void controlSpeed(long loadTime) throws InterruptedException {
        if (loadTime < goalTime) {
            Thread.sleep(goalTime - loadTime);
        }
    }

    private static void validate(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Must be three parameter: Url, speed, file");
        }

        String url = args[0];
        try {
            new URL(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong Url: " + url);
        }

        String speed = args[1];
        try {
            Integer.parseInt(speed);
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong speed: " + speed);
        }

        String file = args[2];
        try {
            Path.of(file).toFile();
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong file name: " + file);
        }
    }
}
