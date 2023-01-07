package ru.job4j.concurrent.wget;

import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;

public class WgetValidator {
    private WgetValidator() {
    }

    public static void validate(String[] args) throws IllegalArgumentException {
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
            Wget.BytePerSecond.valueOf(speedType.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong speed type: " + speedType
                    + ". You must specify " + Arrays.toString(Wget.BytePerSecond.values()));
        }

        String file = args[3];
        try {
            Path.of(file).toFile();
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong file name: " + file);
        }
    }
}
