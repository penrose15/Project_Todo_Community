package com.example.to_do_list;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithAuthUser {
    long usersId() default 1L;
    String email() default "abc@gmail.com";
    String password() default "1234abcd!";
    String roles() default "USER";
}
