// File: src/main/java/com/studentmanagement/exception/ResourceNotFoundExceptionMapper.java
package com.studentmanagement.exception;

import com.studentmanagement.dto.response.ApiResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {
    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(ApiResponse.error(exception.getMessage()))
                .build();
    }
}