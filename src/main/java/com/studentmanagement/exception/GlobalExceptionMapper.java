// File: src/main/java/com/studentmanagement/exception/GlobalExceptionMapper.java
package com.studentmanagement.exception;

import com.studentmanagement.dto.response.ApiResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Internal Server Error: " + exception.getMessage()))
                .build();
    }
}