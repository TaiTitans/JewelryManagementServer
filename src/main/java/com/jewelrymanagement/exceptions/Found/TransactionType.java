package com.jewelrymanagement.exceptions.Found;

public enum TransactionType {
    in("in"),
    out("out");

    public String getDisplayName() {
        return displayName;
    }

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }
}
