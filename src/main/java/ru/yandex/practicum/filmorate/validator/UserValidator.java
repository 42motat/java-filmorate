package ru.yandex.practicum.filmorate.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
// import java.util.Set;

public class UserValidator {

    private final static Logger log = LoggerFactory.getLogger(UserValidator.class);

    public static void validate(User user) {
        emailValidation(user.getEmail());
        loginValidation(user.getLogin());
        dateOfBirthValidation(user.getBirthday());
        if (user.getName() != null) {
            shownNameValidation(user.getName());
        } else {
            user.setName(user.getLogin());
        }
    }

    private static void emailValidation(String email) {
        if (email == null || email.isBlank()) {
            log.error("Не заполнен адрес электронной почты");
            throw new ValidationException("Ошибка валидации адреса электронной почты:" +
                    " электронный адрес не может быть пустым");
        }
        // тут проверка на наличие символа @
        if (!email.contains("@")) {
            log.error("Адрес не содержит символа @");
            throw new ValidationException("Ошибка валидации адреса электронной почты:" +
                    " некорректный адрес электронной почты");
        } /* else if (email.contains("@")) {
            Set<String> emailDomains = Set.of("gmail.com", "mail.ru", "yandex.ru");
            int atPosition = email.indexOf("@");
            String emailDomain = email.substring(atPosition);
            if (!(emailDomains.contains(emailDomain))) {
                log.error("Не заполнен адрес электронной почты");
                throw new ValidationException("Некорректный адрес электронной почты");
            }
        } */
    }

    private static void loginValidation(String login) {
        if (login == null || login.isBlank()) {
            log.error("Логин пользователя не может быть пустым");
            throw new ValidationException("Ошибка валидации имени пользователя");
        }
        // тут проверка на пробелы в логине
         if (login.contains(" ")) {
             log.error("Логин пользователя не может содержать пробелы");
             throw new ValidationException("Ошибка валидации имени пользователя");
         }
    }

    private static void shownNameValidation(String shownName) {
        // в ТЗ не указан способ/условия валидации имени для отображения, поэтому импровизация
        if (shownName.length() > 100) {
            log.error("Никнейм превышает 100 символов");
            throw new ValidationException("Ошибка валидации никнейма");
        }
    }

    private static void dateOfBirthValidation(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            log.error("Указанная дата рождения некорректна");
            throw new ValidationException("Ошибка валидации даты рождения: дата рождения не может быть в будущем");
        }
    }

}
