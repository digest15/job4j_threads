package ru.job4j.concurrent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;

public class Wget implements Runnable {
    private final String url;

    private final String file;

    private final int speed;

    public Wget(String url, int speed, String file) {
        this.url = url;
        this.speed = speed;
        this.file = file;

    }

    public static void main(String[] args) throws InterruptedException {
        validate(args);
        var url = args[0];
        var multiplier = Integer.parseInt(args[1]);
        BytePerSecond bytePerSecond = BytePerSecond.valueOf(args[2].toUpperCase());
        int speed = bytePerSecond.forByte * multiplier;
        var file = args[3];
        Thread wget = new Thread(new Wget(url, speed, file));
        wget.start();
        wget.join();
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {

            int batch = Math.min(speed, BytePerSecond.KBYTE.forByte);
            byte[] dataBuffer = new byte[batch];
            int bytesRead = 0;
            int downloadData = 0;
            long start = System.currentTimeMillis();

            while (!Thread.currentThread().isInterrupted() && (bytesRead) != -1) {
                downloadData += bytesRead;
                if (downloadData >= speed) {
                    long end = System.currentTimeMillis();
                    controlSpeed(end - start);
                    downloadData = 0;
                    start = System.currentTimeMillis();
                }

                bytesRead = in.read(dataBuffer, 0, batch);
                if (bytesRead != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private void controlSpeed(long loadTime) throws InterruptedException {
        if (loadTime < BytePerSecond.MILLIS) {
            Thread.sleep(BytePerSecond.MILLIS - loadTime);
        }
    }

    private static void validate(String[] args) {
        if (args.length < 4) {
            throw new IllegalArgumentException("Must be three parameter: Url, multiplier, speed type, file");
        }

        String url = args[0];
        try {
            new URL(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong Url: " + url);
        }

        String multiplier = args[1];
        try {
            Integer.parseInt(multiplier);
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong speed: " + multiplier);
        }

        String speedType = args[2];
        try {
            BytePerSecond.valueOf(speedType.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong speed type: " + speedType
                    + ". You must specify " + Arrays.toString(BytePerSecond.values()));
        }

        String file = args[3];
        try {
            Path.of(file).toFile();
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong file name: " + file);
        }
    }

    private enum BytePerSecond {
        BYTE(1),
        KBYTE(1024),
        MBYTE(1048576),
        GBYTE(1073741824);

        final int forByte;

        static final int MILLIS = 1000;

        BytePerSecond(int forByte) {
            this.forByte = forByte;
        }
    }
}
