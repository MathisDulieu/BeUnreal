package com.supinfo.beunreal_api_gateway.utils;

import com.supinfo.beunreal_api_gateway.dao.UserDao;
import com.supinfo.beunreal_api_gateway.model.authentication.RegisterRequest;
import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.user.request.UpdateAuthenticatedUserInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}';:\",.<>?|`~])[A-Za-z\\d!@#$%^&*()_+\\-={}';:\",.<>?|`~]{8,}$";
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;


    public String getRegisterValidationError(RegisterRequest request) {
        if (isInvalidEmail(request.getEmail())) {
            return "Invalid email format. Please enter a valid email address.";
        }
        if (isInvalidUsername(request.getUsername())) {
            return "Invalid username. Username must be between 3-12 characters and contain only letters, numbers, and underscores.";
        }
        if (isInvalidPassword(request.getPassword())) {
            return "Password does not meet security requirements. Please use at least 8 characters including uppercase, lowercase, numbers, and special characters.";
        }
        if (userDao.isUsernameAlreadyUsed(request.getUsername())) {
            return "Username already exists. Please choose a different username.";
        }
        if (userDao.isEmailAlreadyUsed(request.getEmail())) {
            return "Email address is already registered. Please use a different email or try to recover your account.";
        }

        return null;
    }

    public boolean isInvalidEmail(String email) {
        return isNull(email) || !Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    public boolean isInvalidUsername(String username) {
        return isNull(username) || username.length() < 3 || username.length() > 11 || username.contains(" ");
    }

    public boolean isInvalidPassword(String password) {
        return isNull(password) || !password.matches(PASSWORD_REGEX);
    }

    public String getErrorsAsString(List<String> errors) {
        return String.join(" | ", errors);
    }

    public void validateUpdateAuthenticatedUserDetailsRequest(List<String> errors, UpdateAuthenticatedUserInfoRequest request) {
        if (isNull(request.getEmail()) && isNull(request.getUsername()) && isNull(request.getNewPassword())) {
            errors.add("At least one field (email, username, or password) must be provided to update your profile. Please specify what you want to change.");
        }
    }

    public void validateNewUsername(List<String> errors, String username, User userToUpdate) {
        if (!isNull(username)) {
            if (isInvalidUsername(username)) {
                errors.add("Invalid username format. Username must be between 3-20 characters and contain only letters, numbers, and underscores.");
            }

            if (userDao.isUsernameAlreadyUsed(username)) {
                errors.add("This username is already taken. Please choose a different username.");
            }

            if (username.equals(userToUpdate.getUsername())) {
                errors.add("The new username is the same as your current username. Please enter a different username to make a change.");
            }

            userToUpdate.setUsername(username);
        }
    }

    public void validateNewEmail(List<String> errors, String email, User userToUpdate) {
        if (!isNull(email)) {
            if (isInvalidEmail(email)) {
                errors.add("Invalid email format. Please enter a valid email address.");
            }

            if (userDao.isEmailAlreadyUsed(email)) {
                errors.add("This email address is already registered with another account. Please use a different email address.");
            }

            if (email.equals(userToUpdate.getEmail())) {
                errors.add("The new email is the same as your current email. Please enter a different email address to make a change.");
            }

            userToUpdate.setEmail(email);
        }
    }

    public void validateNewPassword(List<String> errors, String oldPassword, String newPassword, User userToUpdate) {
        if (!isNull(oldPassword) || !isNull(newPassword)) {
            if (isInvalidPassword(newPassword)) {
                errors.add("Password does not meet security requirements. Please use at least 8 characters including" +
                        " uppercase, lowercase, numbers, and special characters.");
            }

            if (passwordEncoder.matches(newPassword, userToUpdate.getPassword())) {
                errors.add("The new password cannot be the same as your current password. Please choose a different password.");
            }

            if (!passwordEncoder.matches(oldPassword, userToUpdate.getPassword())) {
                errors.add("Incorrect current password. Please enter your current password correctly to verify your identity.");
            }

            userToUpdate.setPassword(passwordEncoder.encode(newPassword));
        }
    }

}
