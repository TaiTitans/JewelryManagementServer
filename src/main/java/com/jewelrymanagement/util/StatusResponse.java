package com.jewelrymanagement.util;

public class StatusResponse<T> {
    public StatusResponse(String status, String message, T data) {
        this.status = String.valueOf(status);
        this.message = message;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = String.valueOf(status);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String status;
    private String message;
    private T data;
}
