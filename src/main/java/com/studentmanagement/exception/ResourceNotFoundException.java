// File: src/main/java/com/studentmanagement/exception/ResourceNotFoundException.java
package com.studentmanagement.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}