package com.jewelrymanagement.exceptions.User;

import com.jewelrymanagement.exceptions.BaseException;

public class DeletedSuccess extends BaseException {
    public DeletedSuccess(int userId){
        super("User with id "+userId+" deleted success");
    }
}
