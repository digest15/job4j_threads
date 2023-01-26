package ru.job4j.concurrent.emailnotification;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

class EmailNotificationTest {
    @Test
    public void whenAdd() {
        try (EmailNotification emailNotification = new EmailNotification(Executors.newCachedThreadPool())) {
            User user = new User("John", "netu@mail.ru");
            emailNotification.emailTo(user);
        }
    }
}