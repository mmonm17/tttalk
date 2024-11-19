package com.t_t_talk.DB;

public class Response {
    boolean success;
    String message;

    public Response(boolean isSuccess, String message) {
        this.success = isSuccess;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }

    public void setSuccess() {
        this.success = true;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
