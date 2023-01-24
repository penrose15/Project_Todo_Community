package com.example.to_do_list.common.exception;

import lombok.Getter;

public enum ExceptionCode {
    USER_NOT_FOUND(404,"user not found");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
