package com.jewelrymanagement.exceptions.User;

import com.jewelrymanagement.exceptions.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(int userId) {
        super("User with id " + userId + " not found.");
    }
}