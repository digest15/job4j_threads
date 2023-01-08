package ru.job4j.concurrent.parsefile;

import java.io.*;
import java.util.function.Predicate;

public class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public String getContent() throws IOException {
        return content(data -> true);
    }

    public String getContentWithoutUnicode() throws IOException {
        return content(data -> data < 0x80);
    }

    private String content(Predicate<Character> filter) throws IOException {
        StringBuilder output = new StringBuilder();
        try (InputStream i = new BufferedInputStream(new FileInputStream(file))) {
            int data = 0;
            while (data >= 0) {
                data = i.read();
                if (filter.test((char) data)) {
                    output.append((char) data);
                }
            }
        }
        return output.toString();
    }
}
