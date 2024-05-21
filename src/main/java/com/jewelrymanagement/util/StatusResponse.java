package com.jewelrymanagement.util;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
public class StatusResponse<T> {


    public StatusResponse(String requestId, String requestDateTime, String status, String message, T data) {
        this.requestId = UUID.randomUUID().toString();
        this.requestDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private final String requestId;
    private final String requestDateTime;
    private String status;
    private String message;
    private T data;
}
