package com.jewelrymanagement.exceptions.User;

public enum Role {
    president("president"),
    manager("manager"),
    staff("staff");

    public String getDisplayName() {
        return displayName;
    }

    private final String displayName;
    Role(String displayName){
        this.displayName = displayName;
    }
}
