package com.blog.medium.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericException extends RuntimeException {
    GenericException(String message) {
        super(message);
    }
}
