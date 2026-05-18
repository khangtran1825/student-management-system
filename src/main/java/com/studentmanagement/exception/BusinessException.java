// File: src/main/java/com/studentmanagement/exception/BusinessException.java
package com.studentmanagement.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}