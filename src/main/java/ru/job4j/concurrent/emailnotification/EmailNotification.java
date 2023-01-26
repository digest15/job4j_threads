package ru.job4j.concurrent.emailnotification;

import java.io.Closeable;
import java.util.concurrent.ExecutorService;

public class EmailNotification implements Closeable {

    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String SUBJECT = String.format("Notification {%s} to email {%s}", USERNAME, EMAIL);
    private static final String BODY = String.format("Add a new event to %s", EMAIL);

    private final ExecutorService pool;

    public EmailNotification(ExecutorService pool) {
        this.pool = pool;
    }

    public void emailTo(User user) {
        pool.submit(() -> buildEmail(user));
    }

    private void send(String subject, String body, String email) {
        /*Empty body*/
    }

    private void buildEmail(User user) {
        String email = user.getEmail();
        String subject = SUBJECT
                .replaceAll("\\{" + USERNAME + "}", user.getName())
                .replaceAll("\\{" + EMAIL + "}", email);
        String body = BODY.replaceAll("\\{" + EMAIL + "}", email);
        send(subject, body, email);
    }

    @Override
    public void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
