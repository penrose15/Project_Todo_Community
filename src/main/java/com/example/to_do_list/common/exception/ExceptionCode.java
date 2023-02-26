package com.example.to_do_list.common.exception;

import lombok.Getter;

public enum ExceptionCode {
    USER_NOT_FOUND(404,"user not found"),
    CATEGORY_INVALID_UPDATE(405, "본인의 category만 수정 가능"),
    USER_ID_NOT_MATCH(403, "유저 아이디 일치하지 않음"),
    CATEGORY_NOT_FOUND(404, "존재하지 않는 카테고리"),
    TEAM_NOT_FOUND(404, " 존재하지 않는 팀"),
    ONLY_TEAM_HOST_AVAILABLE(403, "팀의 방장만이 가능한 권리입니다."),
    NUMBER_OF_USERS_IS_NOT_ZERO(405, "팀원이 0명이어야만 삭제가 가능합니다."),
    TODO_NOT_FOUND(404, "존재하지 않는 todo 입니다."),
    INVALID_TODO_UPDATE(405, "자신의 todo만 변경 가능합니다."),
    INVALID_TODO_DELETE(405, "본인의 todo만 삭제 가능"),
    INVALID_TEAM_JOIN(405, "하나의 팀만 참여 가능합니다."),
    TEAM_IS_FULL(405, "팀이 가득 찼습니다..?"),
    INVALID_EMAIL(404,"존재하지 않는 이메일"),
    EMAIL_ALREADY_EXIST(405, "이메일이 존재합니다."),
    PASSWORD_NOt_MATCH(405, "패스워드가 위와 일치해야 합니다."),
    AUTHCODE_NOT_FOUND(404, "인증번호가 존재하지 않습니다.");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
